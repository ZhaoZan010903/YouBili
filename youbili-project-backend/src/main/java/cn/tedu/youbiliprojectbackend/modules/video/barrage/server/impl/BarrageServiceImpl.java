package cn.tedu.youbiliprojectbackend.modules.video.barrage.server.impl;

import cn.tedu.youbiliprojectbackend.modules.video.barrage.pojo.DTO.BarrageDTO;
import cn.tedu.youbiliprojectbackend.modules.video.barrage.pojo.VO.BarrageVO;
import cn.tedu.youbiliprojectbackend.modules.video.barrage.pojo.entity.Barrage;
import cn.tedu.youbiliprojectbackend.modules.video.barrage.server.BarrageService;
import cn.tedu.youbiliprojectbackend.modules.video.videocount.dao.cache.VideoCountCacheRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class BarrageServiceImpl implements BarrageService {
    @Autowired
    private MongoTemplate template;
    @Autowired
    private VideoCountCacheRepository videoCountCacheRepository;

    @Override
    public List<BarrageVO> findByVideoID(Long videoID) {
        log.debug("开始处理加载弹幕列表功能");
        Sort sort = Sort.by(Sort.Direction.ASC, "time");
        Criteria criteria = new Criteria();
        criteria.and("videoID").is(videoID);
        Query query = new Query(criteria);
        query.with(sort);
        return template.find(query, BarrageVO.class, "barrage");
    }

    @Override
    public void sent(BarrageDTO barrageDTO) {
        log.debug("开始处理新增弹幕功能");
        Barrage barrage = new Barrage();
        BeanUtils.copyProperties(barrageDTO, barrage);
        barrage.setGmtCreate(LocalDateTime.now());
        videoCountCacheRepository.addBarrageCount(barrageDTO.getVideoID());
        template.save(barrage);
    }

}
