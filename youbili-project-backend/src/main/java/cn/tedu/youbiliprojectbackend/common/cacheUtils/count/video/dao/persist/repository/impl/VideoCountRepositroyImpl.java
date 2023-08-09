package cn.tedu.youbiliprojectbackend.common.cacheUtils.count.video.dao.persist.repository.impl;

import cn.tedu.youbiliprojectbackend.common.cacheUtils.count.video.dao.persist.mapper.VideoCountMapper;
import cn.tedu.youbiliprojectbackend.common.cacheUtils.count.video.dao.persist.repository.VideoCountRepositroy;
import cn.tedu.youbiliprojectbackend.common.cacheUtils.count.video.pojo.VideoCount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class VideoCountRepositroyImpl implements VideoCountRepositroy {

    @Autowired
    VideoCountMapper videoCountMapper;

    @Override
    public List<VideoCount> videoCacheCount() {
        return videoCountMapper.selectCount();
    }
}
