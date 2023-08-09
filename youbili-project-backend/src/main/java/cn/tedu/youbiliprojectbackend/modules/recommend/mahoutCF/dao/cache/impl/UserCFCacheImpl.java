package cn.tedu.youbiliprojectbackend.modules.recommend.mahoutCF.dao.cache.impl;


import cn.tedu.youbiliprojectbackend.common.cacheUtils.videoList.pojo.vo.VideoUserCache;
import cn.tedu.youbiliprojectbackend.modules.recommend.mahoutCF.dao.cache.UserCFCache;
import cn.tedu.youbiliprojectbackend.modules.recommend.mahoutCF.score.ListVideoCF;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 协同过滤算法;
 *
 * @author 赵錾
 */
@Slf4j
@Repository
public class UserCFCacheImpl implements UserCFCache {

    @Autowired
    ListVideoCF listVideoCF;

    @Autowired
    RedisTemplate<String, Serializable> redisTemplate;


    /**
     * 从缓存之中将视频列表取出
     *
     * @param userID 用户当事人ID
     * @return 视频列表
     */
    @Override
    public List<VideoUserCache> CFvideoList(Long userID) {
        List<Long> longs = listVideoCF.videoIDList(userID);
        ValueOperations<String, Serializable> operations = redisTemplate.opsForValue();
        List<VideoUserCache> videolistVO = new ArrayList<>();
        for (Long aLong : longs) {
            log.debug("视频ID: {}", aLong);
            Set<String> keys = redisTemplate.keys(VIDEO_LIST + "*:" + aLong);
            for (String key : keys) {
                VideoUserCache serializable = (VideoUserCache) operations.get(key);
                videolistVO.add(serializable);
            }
        }
        return videolistVO;
    }


}
