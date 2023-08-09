package cn.tedu.youbiliprojectbackend.modules.video.uplaod.service.impl;

import cn.tedu.youbiliprojectbackend.modules.video.uplaod.dao.repository.IVideoUpload;
import cn.tedu.youbiliprojectbackend.modules.video.uplaod.pojo.dto.UploadTextDTO;
import cn.tedu.youbiliprojectbackend.modules.video.uplaod.service.IVideoUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VideoUploadServiceImpl implements IVideoUploadService {
    @Autowired
    private IVideoUpload iVideoUpload;

    @Override
    public void videoUpload(UploadTextDTO uploadTextDTO) {
        iVideoUpload.videoUpload(uploadTextDTO);
    }
}
