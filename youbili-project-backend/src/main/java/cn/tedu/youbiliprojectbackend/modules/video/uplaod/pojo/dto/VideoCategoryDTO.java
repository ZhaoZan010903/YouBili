package cn.tedu.youbiliprojectbackend.modules.video.uplaod.pojo.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class VideoCategoryDTO implements Serializable {
    private Long videoID;
    private Long categoryID;
}
