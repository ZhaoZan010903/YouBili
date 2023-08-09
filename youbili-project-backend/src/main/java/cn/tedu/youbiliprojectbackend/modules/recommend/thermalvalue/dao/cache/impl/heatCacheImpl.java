package cn.tedu.youbiliprojectbackend.modules.recommend.thermalvalue.dao.cache.impl;

import cn.tedu.youbiliprojectbackend.common.cacheUtils.count.video.pojo.VideoCount;
import cn.tedu.youbiliprojectbackend.common.cacheUtils.videoList.pojo.vo.VideoUserCache;
import cn.tedu.youbiliprojectbackend.modules.recommend.thermalvalue.dao.cache.heatCache;
import cn.tedu.youbiliprojectbackend.modules.recommend.thermalvalue.dao.persist.repository.ThremaRepository;
import cn.tedu.youbiliprojectbackend.modules.recommend.thermalvalue.pojo.sum.HeatVideo;
import cn.tedu.youbiliprojectbackend.modules.recommend.thermalvalue.pojo.sum.videoDTO;
import cn.tedu.youbiliprojectbackend.modules.recommend.thermalvalue.pojo.sum.videoDay;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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
public class heatCacheImpl implements heatCache {

    @Autowired
    RedisTemplate<String, Serializable> redisTemplate;
    @Autowired
    ThremaRepository thremaRepository;


    /**
     * 返回所有视频
     *
     * @return
     */
    @Override
    public List<videoDTO> listCount() {
        List<videoDTO> videoDTOList = new ArrayList<>();
        List<videoDay> video = thremaRepository.video();
        ValueOperations<String, Serializable> operations = redisTemplate.opsForValue();
        for (videoDay videoDay : video) {
            Set<String> keys = redisTemplate.keys(VIDEO_COUNT + videoDay.getVideoID() + ":*");
            for (String key : keys) {
                VideoCount serializable = (VideoCount) operations.get(key);
                videoDTO videoDTO = new videoDTO();
                BeanUtils.copyProperties(serializable, videoDTO);
                videoDTO.setUploadDate(videoDay.getUploadDate());
                videoDay.getUploadDate();
                videoDTOList.add(videoDTO);
            }
        }
        return videoDTOList;
    }

    /**
     * 查询热力值排行榜 并返回对应数据
     *
     * @return
     */
    @Override
    public List<VideoUserCache> select() {
        ValueOperations<String, Serializable> operations = redisTemplate.opsForValue();
        List<HeatVideo> serializable = (List<HeatVideo>) operations.get(VIDEO_HEAT);
        List<VideoUserCache> countList = new ArrayList<>();
        for (HeatVideo heatVideo : serializable) {

        }


//        for (HeatVideo heatVideo : serializable) {
//            Set<String> keys = redisTemplate.keys(VIDEO_LIST+"*"+heatVideo.getVideoID());
//            for (String key : keys) {
//                VideoUserCache serializable1 = (VideoUserCache) operations.get(key);
//                countList.add(serializable1);
//            }
//        }
        if (countList.size() > 10) {

        }


        return countList;
    }
}

