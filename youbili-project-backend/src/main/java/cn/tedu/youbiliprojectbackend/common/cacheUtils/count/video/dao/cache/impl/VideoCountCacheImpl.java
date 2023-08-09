package cn.tedu.youbiliprojectbackend.common.cacheUtils.count.video.dao.cache.impl;

import cn.tedu.youbiliprojectbackend.common.cacheUtils.count.video.dao.cache.VideoCountCache;
import cn.tedu.youbiliprojectbackend.common.cacheUtils.count.video.pojo.VideoCount;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Slf4j
@Repository
public class VideoCountCacheImpl implements VideoCountCache {


    @Autowired
    RedisTemplate<String, Serializable> redisTemplate;

    boolean status = true;


    /**
     * 将视频计数信息保存在缓存之中
     */
    @Override
    public void save(List<VideoCount> videoCounts) {
        log.debug("开始将视频计数信息保存在缓存之中!");
        ValueOperations<String, Serializable> operations = redisTemplate.opsForValue();
        for (VideoCount count : videoCounts) {
            String key = VIDEO_COUNT + count.getVideoID() + ":";
            operations.set(key + status, count);
        }
    }

    /**
     * 清空所有保存的视频计数信息
     */
    @Override
    public void deleteAll() {
        Set<String> keys = redisTemplate.keys(VIDEO_COUNT + "*" + ":" + "*");
        ValueOperations<String, Serializable> operations = redisTemplate.opsForValue();
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }

    }
}
