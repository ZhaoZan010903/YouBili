package com.youbili.modules.user.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FollowUserVO {

    private Long followId;
    private Long userId;
    private String nickname;
    private String avatarUrl;
    private Integer accountLevel;
    private Integer fansCount;
    private Integer followCount;
    private Boolean mutualFollow;
    private LocalDateTime followDate;
}
