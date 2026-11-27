package com.youbili.modules.video.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class UpdateVideoRequest {

    @NotBlank(message = "视频标题不能为空")
    @Size(min = 2, max = 100, message = "视频标题长度需在 2-100 字之间")
    private String title;

    @Size(max = 1000, message = "视频简介不能超过 1000 字")
    private String description;

    private String videoImgUrl;
    private Long categoryId;
    private List<Long> tagIds;
}
