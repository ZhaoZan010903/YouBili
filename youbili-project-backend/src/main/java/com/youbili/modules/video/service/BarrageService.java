package com.youbili.modules.video.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.youbili.modules.video.dto.SendBarrageRequest;
import com.youbili.modules.video.entity.BarrageDocument;
import com.youbili.modules.video.entity.BarrageRecord;
import com.youbili.modules.video.mapper.BarrageMapper;
import com.youbili.modules.video.mapper.VideoMapper;
import com.youbili.modules.video.vo.BarrageVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BarrageService {

    private final MongoTemplate mongoTemplate;
    private final BarrageMapper barrageMapper;
    private final VideoMapper videoMapper;

    @Value("${youbili.mongodb.barrage-enabled:true}")
    private boolean mongoEnabled;

    public List<BarrageVO> listByVideoId(Long videoId) {
        if (mongoEnabled) {
            try {
                Query query = new Query(Criteria.where("videoId").is(videoId))
                        .with(Sort.by(Sort.Direction.ASC, "time"));
                List<BarrageDocument> docs = mongoTemplate.find(query, BarrageDocument.class);
                return docs.stream()
                        .map(d -> BarrageVO.builder()
                                .time(d.getTime())
                                .text(d.getText())
                                .color(d.getColor())
                                .type(d.getType())
                                .userId(d.getUserId())
                                .build())
                        .toList();
            } catch (Exception e) {
                log.warn("MongoDB unavailable for barrage query, falling back to PostgreSQL: {}", e.getMessage());
            }
        }
        return barrageMapper.selectList(
                new LambdaQueryWrapper<BarrageRecord>()
                        .eq(BarrageRecord::getVideoId, videoId)
                        .eq(BarrageRecord::getEnable, 1)
                        .orderByAsc(BarrageRecord::getBarrageTime)
        ).stream().map(r -> BarrageVO.builder()
                .time(r.getBarrageTime())
                .text(r.getBarrageText())
                .color(r.getColor())
                .type(r.getType())
                .userId(r.getUserId())
                .build()).toList();
    }

    @Transactional(rollbackFor = Exception.class)
    public BarrageVO send(Long videoId, Long userId, SendBarrageRequest request) {
        String color = request.getColor() == null ? "#FFFFFF" : request.getColor();
        String type = request.getType() == null ? "scroll" : request.getType();
        LocalDateTime now = LocalDateTime.now();

        boolean savedToMongo = false;
        if (mongoEnabled) {
            try {
                BarrageDocument doc = new BarrageDocument();
                doc.setVideoId(videoId);
                doc.setUserId(userId);
                doc.setTime(request.getTime());
                doc.setText(request.getText().trim());
                doc.setColor(color);
                doc.setType(type);
                doc.setGmtCreate(now);
                mongoTemplate.save(doc);
                savedToMongo = true;
            } catch (Exception e) {
                log.warn("MongoDB unavailable while saving barrage, falling back to PostgreSQL: {}", e.getMessage());
            }
        }

        if (!savedToMongo) {
            BarrageRecord record = new BarrageRecord();
            record.setVideoId(videoId);
            record.setUserId(userId);
            record.setBarrageTime(request.getTime());
            record.setBarrageText(request.getText().trim());
            record.setColor(color);
            record.setType(type);
            record.setEnable(1);
            barrageMapper.insert(record);
        }

        videoMapper.adjustBarrageCount(videoId, 1);

        return BarrageVO.builder()
                .time(request.getTime())
                .text(request.getText().trim())
                .color(color)
                .type(type)
                .userId(userId)
                .build();
    }
}
