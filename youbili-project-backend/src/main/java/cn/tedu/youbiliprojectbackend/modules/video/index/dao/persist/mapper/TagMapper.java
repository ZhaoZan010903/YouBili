package cn.tedu.youbiliprojectbackend.modules.video.index.dao.persist.mapper;

import cn.tedu.youbiliprojectbackend.modules.video.index.pojo.vo.VideoTagVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagMapper {
    List<VideoTagVO> select(Long videoID);
}
