package cn.tedu.youbiliprojectbackend.modules.video.index.dao.cache.impl;

import cn.tedu.youbiliprojectbackend.common.cacheUtils.count.user.pojo.vo.UserCountsCacheVO;
import cn.tedu.youbiliprojectbackend.common.cacheUtils.count.video.pojo.VideoCount;
import cn.tedu.youbiliprojectbackend.common.cacheUtils.videoList.pojo.vo.VideoUserCache;
import cn.tedu.youbiliprojectbackend.common.ex.ServiceException;
import cn.tedu.youbiliprojectbackend.common.web.response.ServiceCode;
import cn.tedu.youbiliprojectbackend.modules.video.index.dao.cache.IVideoListCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.Set;


@Repository
@Slf4j
public class VideoListCache implements IVideoListCache {

    @Autowired
    RedisTemplate<String, Serializable> redisTemplate;

    @Override
    public VideoCount listLike(Long videoID) {
        ValueOperations<String, Serializable> opsForValue = redisTemplate.opsForValue();
        log.debug("---------videoID:{}", videoID);
        VideoCount vo = null;
        try {
            log.debug("开始------------");
            vo = (VideoCount) opsForValue.get(VIDEO_COUNT + videoID + ":" + true);
            vo.getVideoID();
            if (vo == null) {
                throw new RuntimeException();
            }
        } catch (Throwable e) {
            log.debug("错误~~~~~");
            try {
                vo = (VideoCount) opsForValue.get(VIDEO_COUNT + videoID + ":" + false);
                if (vo == null) {
                    throw new RuntimeException();
                }
            } catch (Throwable e1) {
                throw new ServiceException(ServiceCode.ERROR_CONFLICT, "数据不存在");
            }
        }
        return vo;
    }

    @Override
    public VideoUserCache videoListselect(Long videoID) {
        Set<String> keys = redisTemplate.keys(VIDEO_LIST + "*:" + videoID);
        ValueOperations<String, Serializable> operations = redisTemplate.opsForValue();
        VideoUserCache videoUserCache = null;
        for (String key : keys) {
            Serializable serializable = operations.get(key);
            if (serializable != null) {
                videoUserCache = (VideoUserCache) serializable;
            }
        }
        log.debug("{}", videoUserCache);
        return videoUserCache;
    }

    @Override
    public UserCountsCacheVO listFansFolCount(Long userID) {
        ValueOperations<String, Serializable> opsForValue = redisTemplate.opsForValue();
        UserCountsCacheVO vo = null;
        try {
            vo = (UserCountsCacheVO) opsForValue.get(USER_COUNT + userID + ":" + true);
        } catch (RuntimeException e) {
            try {
                vo = (UserCountsCacheVO) opsForValue.get(USER_COUNT + userID + ":" + false);
            } catch (RuntimeException e1) {
                throw new ServiceException(ServiceCode.ERROR_CONFLICT, "数据不存在");
            }
        }
        return vo;
    }
}
