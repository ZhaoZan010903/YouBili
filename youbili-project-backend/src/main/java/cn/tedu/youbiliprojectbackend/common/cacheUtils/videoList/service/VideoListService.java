package cn.tedu.youbiliprojectbackend.common.cacheUtils.videoList.service;


public interface VideoListService {
    /**
     * 项目启动时,用于将视频列表放入缓存之中
     */
    void saveCacheVideo();
}
