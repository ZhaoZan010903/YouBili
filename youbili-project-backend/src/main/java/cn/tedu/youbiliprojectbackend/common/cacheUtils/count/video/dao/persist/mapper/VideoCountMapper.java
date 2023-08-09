package cn.tedu.youbiliprojectbackend.common.cacheUtils.count.video.dao.persist.mapper;


import cn.tedu.youbiliprojectbackend.common.cacheUtils.count.video.pojo.VideoCount;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * 视频计数字段Mapper
 */
@Repository
public interface VideoCountMapper {
    List<VideoCount> selectCount();
}
