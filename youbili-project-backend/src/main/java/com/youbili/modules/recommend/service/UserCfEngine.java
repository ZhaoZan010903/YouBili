package com.youbili.modules.recommend.service;

import com.youbili.modules.recommend.entity.UserBehaviorScore;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 纯 Java 实现的基于用户的余弦相似度协同过滤（User-Based CF）推荐引擎，
 * 替代已废弃且与 JDK 21 不兼容的 Apache Mahout 0.13 MapReduce 依赖。
 */
@Component
public class UserCfEngine {

    private static final int MAX_NEIGHBORS = 30;

    public List<Long> recommendVideoIds(Long targetUserId,
                                        List<UserBehaviorScore> behaviors,
                                        int topN) {
        if (targetUserId == null || behaviors == null || behaviors.isEmpty() || topN <= 0) {
            return Collections.emptyList();
        }

        // userId -> (videoId -> score)
        Map<Long, Map<Long, Double>> userVectors = new HashMap<>();
        for (UserBehaviorScore b : behaviors) {
            if (b.getUserId() == null || b.getVideoId() == null || b.getScore() == null) {
                continue;
            }
            userVectors.computeIfAbsent(b.getUserId(), k -> new HashMap<>())
                    .merge(b.getVideoId(), b.getScore(), Double::sum);
        }

        Map<Long, Double> targetVector = userVectors.get(targetUserId);
        if (targetVector == null || targetVector.isEmpty()) {
            return Collections.emptyList();
        }

        // 计算目标用户与其他用户的余弦相似度
        List<Map.Entry<Long, Double>> neighbors = userVectors.entrySet().stream()
                .filter(e -> !e.getKey().equals(targetUserId))
                .map(e -> Map.entry(e.getKey(), cosineSimilarity(targetVector, e.getValue())))
                .filter(e -> e.getValue() > 0.0)
                .sorted(Map.Entry.<Long, Double>comparingByValue().reversed())
                .limit(MAX_NEIGHBORS)
                .toList();

        if (neighbors.isEmpty()) {
            return Collections.emptyList();
        }

        // 对目标用户尚未交互过的候选视频做相似度加权打分
        Map<Long, Double> candidateScores = new HashMap<>();
        for (Map.Entry<Long, Double> neighbor : neighbors) {
            double sim = neighbor.getValue();
            Map<Long, Double> neighborItems = userVectors.get(neighbor.getKey());
            for (Map.Entry<Long, Double> item : neighborItems.entrySet()) {
                Long videoId = item.getKey();
                if (!targetVector.containsKey(videoId) && item.getValue() > 0) {
                    candidateScores.merge(videoId, sim * item.getValue(), Double::sum);
                }
            }
        }

        return candidateScores.entrySet().stream()
                .sorted(Map.Entry.<Long, Double>comparingByValue(Comparator.reverseOrder()))
                .limit(topN)
                .map(Map.Entry::getKey)
                .toList();
    }

    private double cosineSimilarity(Map<Long, Double> a, Map<Long, Double> b) {
        double dot = 0.0;
        double normA = 0.0;
        double normB = 0.0;
        for (Map.Entry<Long, Double> entry : a.entrySet()) {
            double va = entry.getValue();
            normA += va * va;
            Double vb = b.get(entry.getKey());
            if (vb != null) {
                dot += va * vb;
            }
        }
        for (double vb : b.values()) {
            normB += vb * vb;
        }
        if (normA == 0.0 || normB == 0.0) {
            return 0.0;
        }
        return dot / (Math.sqrt(normA) * Math.sqrt(normB));
    }
}
