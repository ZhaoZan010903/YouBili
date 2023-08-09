package cn.tedu.youbiliprojectbackend.common.cacheUtils.videoList.service.impl;

import cn.tedu.youbiliprojectbackend.common.cacheUtils.videoList.dao.cache.VideoListCacheRepository;
import cn.tedu.youbiliprojectbackend.common.cacheUtils.videoList.dao.persist.repository.VideoRepositroy;
import cn.tedu.youbiliprojectbackend.common.cacheUtils.videoList.service.VideoListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class VideoListServicImpl implements VideoListService {
    @Autowired
    VideoListCacheRepository cacheRepository;
    @Autowired
    VideoRepositroy videoRepositroy;


    /**
     * 项目启动时,用于将视频列表放入缓存之中
     */
    @Override
    public void saveCacheVideo() {
        cacheRepository.deleteAll();
        cacheRepository.save(videoRepositroy.videoCache());
    }
}
