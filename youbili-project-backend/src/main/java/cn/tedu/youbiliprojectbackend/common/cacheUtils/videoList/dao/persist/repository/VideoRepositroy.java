package cn.tedu.youbiliprojectbackend.common.cacheUtils.videoList.dao.persist.repository;


import cn.tedu.youbiliprojectbackend.common.cacheUtils.videoList.pojo.vo.VideoUserCache;

import java.util.List;

public interface VideoRepositroy {
    /**
     * 查询所有视频列表集合用于存入Redis缓存之中;
     */
    List<VideoUserCache> videoCache();
}
