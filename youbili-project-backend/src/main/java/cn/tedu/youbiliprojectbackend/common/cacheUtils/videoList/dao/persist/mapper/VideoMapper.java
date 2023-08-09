package cn.tedu.youbiliprojectbackend.common.cacheUtils.videoList.dao.persist.mapper;

import cn.tedu.youbiliprojectbackend.common.cacheUtils.videoList.pojo.vo.VideoUserCache;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 视频类mapper
 *
 * @author 赵錾
 */
@Repository
public interface VideoMapper {
    List<VideoUserCache> selectAll();
}
