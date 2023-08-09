package cn.tedu.youbiliprojectbackend.modules.video.uplaod.dao.mapper;

import cn.tedu.youbiliprojectbackend.modules.video.uplaod.pojo.dto.UploadTextDTO;
import cn.tedu.youbiliprojectbackend.modules.video.uplaod.pojo.dto.VideoUpdateDTO;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoUploadMapper {
    int insertVideoContent(UploadTextDTO uploadTextDTO);

    int changeVideoContent(VideoUpdateDTO videoUpdateDTO);

}
