package com.youbili.modules.video.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class VideoCardVO implements Serializable {

    private Long videoId;
    private Long userId;
    private String title;
    private String description;
    private String duration;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime uploadDate;

    private String videoSrcUrl;
    private String videoImgUrl;
    private String authorNickname;
    private String authorAvatarUrl;
    private Long categoryId;
    private String categoryName;
    private Integer viewCount;
    private Integer likeCount;
    private Integer favoriteCount;
    private Integer barrageCount;
    private Integer commentCount;
    private String reviewStatus;
    private Double heatScore;
}
