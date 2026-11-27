package com.youbili.modules.recommend.entity;

import lombok.Data;

@Data
public class UserBehaviorScore {

    private Long userId;
    private Long videoId;
    private Double score;
}
