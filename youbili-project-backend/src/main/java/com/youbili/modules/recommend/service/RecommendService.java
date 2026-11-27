package com.youbili.modules.recommend.service;

import com.youbili.modules.recommend.entity.UserBehaviorScore;
import com.youbili.modules.recommend.mapper.RecommendMapper;
import com.youbili.modules.video.mapper.VideoMapper;
import com.youbili.modules.video.vo.VideoCardVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendService {

    private final RecommendMapper recommendMapper;
    private final VideoMapper videoMapper;
    private final UserCfEngine userCfEngine;

    public List<VideoCardVO> getPersonalizedFeed(Long userId, int size) {
        int safeSize = Math.min(Math.max(size, 1), 40);
        Map<Long, VideoCardVO> resultMap = new LinkedHashMap<>();

        if (userId != null) {
            List<UserBehaviorScore> behaviors = recommendMapper.selectAggregatedUserBehaviors(20_000);
            List<Long> cfIds = userCfEngine.recommendVideoIds(userId, behaviors, safeSize);
            if (!cfIds.isEmpty()) {
                Map<Long, VideoCardVO> fetched = videoMapper.selectCardsByIds(cfIds).stream()
                        .collect(Collectors.toMap(VideoCardVO::getVideoId, Function.identity(), (a, b) -> a));
                for (Long id : cfIds) {
                    VideoCardVO card = fetched.get(id);
                    if (card != null) {
                        resultMap.put(id, card);
                    }
                }
            }
        }

        // 冷启动或推荐数量不足时，用热门与最新审核通过视频补齐
        if (resultMap.size() < safeSize) {
            List<VideoCardVO> fallback = getHotVideos(safeSize * 2);
            for (VideoCardVO card : fallback) {
                if (resultMap.size() >= safeSize) {
                    break;
                }
                resultMap.putIfAbsent(card.getVideoId(), card);
            }
        }

        return new ArrayList<>(resultMap.values());
    }

    public List<VideoCardVO> getCarousel(int size) {
        int safeSize = Math.min(Math.max(size, 1), 10);
        return getHotVideos(safeSize);
    }

    public List<VideoCardVO> getRelatedVideos(Long excludeVideoId, Long userId, int size) {
        int safeSize = Math.min(Math.max(size, 1), 20);
        List<VideoCardVO> candidates = getPersonalizedFeed(userId, safeSize + 2);
        return candidates.stream()
                .filter(v -> !Objects.equals(v.getVideoId(), excludeVideoId))
                .limit(safeSize)
                .toList();
    }

    public List<VideoCardVO> getHotVideos(int size) {
        int safeSize = Math.min(Math.max(size, 1), 50);
        List<VideoCardVO> recent = videoMapper.selectRecentApprovedForHeat(30, 200);
        if (recent.size() < safeSize) {
            recent = videoMapper.selectRecentApprovedForHeat(0, 200);
        }

        LocalDateTime now = LocalDateTime.now();
        for (VideoCardVO card : recent) {
            card.setHeatScore(computeGravityHeat(card, now));
        }

        return recent.stream()
                .sorted(Comparator.comparingDouble((VideoCardVO v) -> v.getHeatScore() == null ? 0.0 : v.getHeatScore())
                        .reversed())
                .limit(safeSize)
                .toList();
    }

    /**
     * 重力衰减热度模型：
     * score = (0.1*view + 0.3*like + 0.5*favorite + 0.4*comment + 0.6*barrage + 1.0) / (ageHours + 2)^1.2
     */
    private double computeGravityHeat(VideoCardVO v, LocalDateTime now) {
        double engagement = safe(v.getViewCount()) * 0.1
                + safe(v.getLikeCount()) * 0.3
                + safe(v.getFavoriteCount()) * 0.5
                + safe(v.getCommentCount()) * 0.4
                + safe(v.getBarrageCount()) * 0.6
                + 1.0;

        double ageHours = 0.0;
        if (v.getUploadDate() != null) {
            long minutes = Math.max(0L, Duration.between(v.getUploadDate(), now).toMinutes());
            ageHours = minutes / 60.0;
        }
        double score = engagement / Math.pow(ageHours + 2.0, 1.2);
        return Math.round(score * 100.0) / 100.0;
    }

    private int safe(Integer value) {
        return value == null ? 0 : value;
    }
}
