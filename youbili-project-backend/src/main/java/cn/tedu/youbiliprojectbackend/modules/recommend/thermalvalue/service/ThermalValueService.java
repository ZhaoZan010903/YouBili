package cn.tedu.youbiliprojectbackend.modules.recommend.thermalvalue.service;

import cn.tedu.youbiliprojectbackend.common.cacheUtils.videoList.pojo.vo.VideoUserCache;

import java.util.List;

/**
 * 热力值算法接口
 */
public interface ThermalValueService {
    /**
     *
     */
    void Runner();

    /**
     * 根据热度值ID查找视频列表
     */
    List<VideoUserCache> select();


    List<VideoUserCache> select1();
}
