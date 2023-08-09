package cn.tedu.youbiliprojectbackend.modules.recommend.thermalvalue.dao.cache.impl;

import cn.tedu.youbiliprojectbackend.common.cacheUtils.videoList.pojo.vo.VideoUserCache;
import cn.tedu.youbiliprojectbackend.modules.recommend.thermalvalue.dao.cache.Select;
import cn.tedu.youbiliprojectbackend.modules.recommend.thermalvalue.pojo.sum.HeatVideo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Repository
@Slf4j
public class SelectImpl implements Select {


    @Autowired
    RedisTemplate<String, Serializable> redisTemplate;

    /**
     * 根据热度值排行榜查询VideoID
     * 根据videoID查询出视频列表
     *
     * @return
     */
    @Override
    public List<VideoUserCache> Select() {
        ValueOperations<String, Serializable> operations = redisTemplate.opsForValue();
        List<HeatVideo> serializable = (List<HeatVideo>) operations.get(VIDEO_HEAT);
        List<VideoUserCache> videoUserCaches = new ArrayList<>();
        for (HeatVideo heatVideo : serializable) {
            Set<String> keys = redisTemplate.keys(VIDEO_LIST + "*:" + heatVideo.getVideoID());
            for (String key : keys) {
                VideoUserCache videoUserCache = (VideoUserCache) operations.get(key);
                videoUserCaches.add(videoUserCache);
            }
        }
        return videoUserCaches;

    }
}
