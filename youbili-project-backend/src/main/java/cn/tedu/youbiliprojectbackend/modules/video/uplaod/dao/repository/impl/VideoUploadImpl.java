package cn.tedu.youbiliprojectbackend.modules.video.uplaod.dao.repository.impl;

import cn.tedu.youbiliprojectbackend.modules.video.uplaod.dao.mapper.VideoUploadMapper;
import cn.tedu.youbiliprojectbackend.modules.video.uplaod.dao.repository.IVideoUpload;
import cn.tedu.youbiliprojectbackend.modules.video.uplaod.pojo.dto.UploadTextDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class VideoUploadImpl implements IVideoUpload {
    @Autowired
    private VideoUploadMapper videoUploadMapper;

    @Override
    public void videoUpload(UploadTextDTO uploadTextDTO) {
        videoUploadMapper.insertVideoContent(uploadTextDTO);
    }
}
