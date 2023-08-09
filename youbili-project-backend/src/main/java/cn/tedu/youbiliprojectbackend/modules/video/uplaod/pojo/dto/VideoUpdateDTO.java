package cn.tedu.youbiliprojectbackend.modules.video.uplaod.pojo.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class VideoUpdateDTO implements Serializable {
    // 视频id
    private Long videoID;
    // 视频时长
    private String duration;
    // 视频本地路径
    private String videoSrcUrl;
    //图片本地路径
    private String videoImgUrl;
}
