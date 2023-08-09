package cn.tedu.youbiliprojectbackend.modules.video.uplaod.service;

import cn.tedu.youbiliprojectbackend.modules.video.uplaod.pojo.dto.UploadTextDTO;

public interface IVideoUploadService {
    void videoUpload(UploadTextDTO uploadTextDTO);
}
