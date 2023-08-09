package cn.tedu.youbiliprojectbackend.common.cacheUtils.count.video.service.impl;

import cn.tedu.youbiliprojectbackend.common.cacheUtils.count.video.dao.cache.VideoCountCache;
import cn.tedu.youbiliprojectbackend.common.cacheUtils.count.video.dao.persist.repository.VideoCountRepositroy;
import cn.tedu.youbiliprojectbackend.common.cacheUtils.count.video.service.VideoCountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class VideoCountServiceImpl implements VideoCountService {

    @Autowired
    VideoCountRepositroy videoCountRepositroy;
    @Autowired
    VideoCountCache videoCountCache;

    /**
     * 当项目启动时 先清除videoCount字段 再保存
     */
    @Override
    public void SaveCacheVideoCount() {
        log.debug("开始向缓存之中写入数据!");
        videoCountCache.deleteAll();
        videoCountCache.save(videoCountRepositroy.videoCacheCount());
    }
}
