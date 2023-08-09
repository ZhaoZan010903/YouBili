package cn.tedu.youbiliprojectbackend.common.cacheUtils.videoList.dao.cache.impl;

import cn.tedu.youbiliprojectbackend.common.cacheUtils.videoList.dao.cache.VideoListCacheRepository;
import cn.tedu.youbiliprojectbackend.common.cacheUtils.videoList.pojo.vo.VideoUserCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * 视频列表缓存实现
 */
@Slf4j
@Repository
public class VideoListCacheRepositoryImpl implements VideoListCacheRepository {
    @Autowired
    RedisTemplate<String, Serializable> template;

    /**
     * 将所有视频放入Redis之中
     *
     * @param videoUserCaches
     */
    @Override
    public void save(List<VideoUserCache> videoUserCaches) {
        log.debug("开始将视频插入缓存之中");
        ValueOperations<String, Serializable> operations = template.opsForValue();
        for (VideoUserCache vo : videoUserCaches) {
            String key = VIDEO_LIST + vo.getUserID() + ":" + vo.getVideoID();
            operations.set(key, vo);
        }
    }

    /**
     * 清空缓存之中所有的视频列表
     */
    @Override
    public void deleteAll() {
        log.debug("清空缓存");
        Set<String> keys = template.keys(VIDEO_LIST + "*:*");
        if (keys != null && !keys.isEmpty()) {
            template.delete(keys);
        }
    }
}
