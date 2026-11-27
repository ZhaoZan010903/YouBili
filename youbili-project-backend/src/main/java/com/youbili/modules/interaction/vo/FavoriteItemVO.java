package com.youbili.modules.interaction.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FavoriteItemVO {

    private Long favoriteId;
    private Long videoId;
    private String title;
    private String description;
    private String videoImgUrl;
    private String duration;
    private Long authorId;
    private String authorNickname;
    private String authorAvatarUrl;
    private Integer viewCount;
    private Integer likeCount;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime favoriteDate;
}
