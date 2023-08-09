package cn.tedu.youbiliprojectbackend.common.cacheUtils.count.video.dao.cache;

import cn.tedu.youbiliprojectbackend.common.cacheUtils.count.video.pojo.VideoCount;
import cn.tedu.youbiliprojectbackend.common.consts.CountConsts;

import java.util.List;

public interface VideoCountCache extends CountConsts {
    /**
     * 将视频计数信息保存在缓存之中
     */
    void save(List<VideoCount> videoCounts);

    /**
     * 清空所有保存的视频计数信息
     */
    void deleteAll();
}
