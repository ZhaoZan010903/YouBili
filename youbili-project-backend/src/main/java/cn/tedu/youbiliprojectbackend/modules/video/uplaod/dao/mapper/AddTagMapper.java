package cn.tedu.youbiliprojectbackend.modules.video.uplaod.dao.mapper;

import cn.tedu.youbiliprojectbackend.modules.video.uplaod.pojo.dto.VideoTagDTO;
import org.springframework.stereotype.Repository;

@Repository
public interface AddTagMapper {
    int insertTag(VideoTagDTO videoTagDTO);
}
