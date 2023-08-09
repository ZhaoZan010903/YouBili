package cn.tedu.youbiliprojectbackend.config;

import cn.tedu.youbiliprojectbackend.common.cacheUtils.videoList.dao.cache.VideoListCacheRepository;
import cn.tedu.youbiliprojectbackend.common.consts.VideoConsts;
import cn.tedu.youbiliprojectbackend.common.cacheUtils.videoList.dao.persist.mapper.VideoMapper;
import cn.tedu.youbiliprojectbackend.common.cacheUtils.videoList.pojo.vo.VideoUserCache;
import cn.tedu.youbiliprojectbackend.modules.video.videocount.dao.cache.VideoCountCacheRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.io.Serializable;
import java.util.List;

@SpringBootTest
@Slf4j
public class video implements VideoConsts {
    @Autowired
    VideoListCacheRepository cacheRepositroy;
    @Autowired
    VideoMapper videoMapper;
    @Autowired
    RedisTemplate<String, Serializable> redisTemplate;

    @Autowired
    VideoCountCacheRepository videoCountCacheRepository;

    @Test
    void video() {

        List<VideoUserCache> videoCaches = videoMapper.selectAll();
        ValueOperations<String, Serializable> operations = redisTemplate.opsForValue();

        for (VideoUserCache vo : videoCaches) {
            String key = VIDEO_LIST + vo.getUserID() + ":" + vo.getVideoID();
            operations.set(key, vo);
//            vo.setDuration(LocalDateTime.parse(vo.getDuration().toString()));
        }

    }


}
