package com.youbili.modules.video.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class PublishVideoRequest {

    @NotBlank(message = "请输入视频标题")
    @Size(min = 2, max = 100, message = "视频标题长度需在 2-100 字之间")
    private String title;

    @NotBlank(message = "请输入视频简介")
    @Size(max = 1000, message = "视频简介不能超过 1000 字")
    private String description;

    @NotBlank(message = "请先上传视频文件")
    private String videoSrcUrl;

    @NotBlank(message = "请先上传视频封面")
    private String videoImgUrl;

    private String duration;

    @NotNull(message = "请选择视频分区")
    private Long categoryId;

    private List<Long> tagIds;
}
