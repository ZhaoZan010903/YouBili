package com.youbili.modules.interaction.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HistoryItemVO {

    private Long historyId;
    private Long videoId;
    private String title;
    private String videoImgUrl;
    private String duration;
    private Long authorId;
    private String authorNickname;
    private String authorAvatarUrl;
    private Integer viewCount;
    private String watchDuration;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime watchDate;
}
