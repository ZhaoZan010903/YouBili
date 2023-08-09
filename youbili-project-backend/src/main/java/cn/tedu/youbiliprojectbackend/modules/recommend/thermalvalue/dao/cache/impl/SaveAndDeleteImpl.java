package cn.tedu.youbiliprojectbackend.modules.recommend.thermalvalue.dao.cache.impl;

import cn.tedu.youbiliprojectbackend.modules.recommend.thermalvalue.dao.cache.SaveAndDelete;
import cn.tedu.youbiliprojectbackend.modules.recommend.thermalvalue.pojo.sum.HeatVideo;
import cn.tedu.youbiliprojectbackend.modules.recommend.thermalvalue.score.ThermalValue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class SaveAndDeleteImpl implements SaveAndDelete {
    @Autowired
    RedisTemplate<String, Serializable> redisTemplate;
    @Autowired
    ThermalValue thermalValue;


    /**
     * 保存7天热力值到Redis之中
     */
    @Override
    public void save() {
        List<HeatVideo> heatVideos = thermalValue.heatValue();
        heatVideos = heatVideos.stream().sorted(Comparator.comparing(HeatVideo::getHeatDay).reversed()).collect(Collectors.toList());
        while (heatVideos.size() > 10) {
            int i = 0;
            i++;
            heatVideos.remove(heatVideos.size() - i);
        }

        ValueOperations<String, Serializable> operations = redisTemplate.opsForValue();
        operations.set(VIDEO_HEAT, (Serializable) heatVideos);
    }

    @Override
    public void delete() {
        redisTemplate.delete(VIDEO_HEAT);
    }
}

