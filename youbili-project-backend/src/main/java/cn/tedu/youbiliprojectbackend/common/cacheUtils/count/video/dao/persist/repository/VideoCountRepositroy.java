package cn.tedu.youbiliprojectbackend.common.cacheUtils.count.video.dao.persist.repository;


import cn.tedu.youbiliprojectbackend.common.cacheUtils.count.video.pojo.VideoCount;

import java.util.List;

public interface VideoCountRepositroy {
    /**
     * 查询视频所有的计数字段;
     *
     * @return
     */
    List<VideoCount> videoCacheCount();
}
