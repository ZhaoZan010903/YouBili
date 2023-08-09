package cn.tedu.youbiliprojectbackend.common.cacheUtils.videoList.dao.cache;


import cn.tedu.youbiliprojectbackend.common.cacheUtils.videoList.pojo.vo.VideoUserCache;
import cn.tedu.youbiliprojectbackend.common.consts.VideoConsts;

import java.util.List;

/**
 * 视频列表缓存
 */
public interface VideoListCacheRepository extends VideoConsts {
    /**
     * 将所有视频放入Redis之中
     */
    void save(List<VideoUserCache> videoUserCaches);

    void deleteAll();
}
