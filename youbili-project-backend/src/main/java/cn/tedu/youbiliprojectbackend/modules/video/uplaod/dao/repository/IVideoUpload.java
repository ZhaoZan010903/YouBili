package cn.tedu.youbiliprojectbackend.modules.video.uplaod.dao.repository;

import cn.tedu.youbiliprojectbackend.modules.video.uplaod.pojo.dto.UploadTextDTO;

public interface IVideoUpload {
    void videoUpload(UploadTextDTO uploadTextDTO);
}
