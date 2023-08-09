package cn.tedu.youbiliprojectbackend.common.cacheUtils.videoList.dao.persist.repository.impl;

import cn.tedu.youbiliprojectbackend.common.cacheUtils.videoList.dao.persist.mapper.VideoMapper;
import cn.tedu.youbiliprojectbackend.common.cacheUtils.videoList.dao.persist.repository.VideoRepositroy;
import cn.tedu.youbiliprojectbackend.common.cacheUtils.videoList.pojo.vo.VideoUserCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class VideoRepositroyImpl implements VideoRepositroy {
    @Autowired
    VideoMapper videoMapper;


    /**
     * 查询所有视频列表集合用于存入Redis缓存之中;
     */
    @Override
    public List<VideoUserCache> videoCache() {
        List<VideoUserCache> videoUserCaches = videoMapper.selectAll();
        return videoUserCaches;
    }
}
