package cn.tedu.youbiliprojectbackend.modules.video.uplaod.pojo.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UploadContentDTO implements Serializable {
    // 视频id
    private Long videoID;
    // 用户id
    private Long userID;
    // 视频标题
    private String title;
    // 视频描述
    private String description;
    // 封面
    private String videoImgUrl;
    // 视频本地路径
    private String videoSrcUrl;
    // 分类
    private Long type;
    // 标签
    private String[] tag;
}
