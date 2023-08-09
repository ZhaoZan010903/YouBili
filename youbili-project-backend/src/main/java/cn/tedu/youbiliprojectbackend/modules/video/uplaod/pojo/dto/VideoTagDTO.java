package cn.tedu.youbiliprojectbackend.modules.video.uplaod.pojo.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class VideoTagDTO implements Serializable {
    private Long videoID;
    private Long tagID;
}
