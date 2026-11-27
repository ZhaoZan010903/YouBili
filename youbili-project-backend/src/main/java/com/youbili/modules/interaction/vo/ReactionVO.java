package com.youbili.modules.interaction.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReactionVO {

    private Boolean liked;
    private Boolean disliked;
    private Boolean favorited;
    private Integer likeCount;
    private Integer dislikeCount;
    private Integer favoriteCount;
}
