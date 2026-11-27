package com.youbili.modules.video.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.youbili.modules.category.entity.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoDetailVO {

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
    private Integer authorFans;
    private Integer authorFollows;
    private Long categoryId;
    private String categoryName;
    private Integer viewCount;
    private Integer likeCount;
    private Integer dislikeCount;
    private Integer favoriteCount;
    private Integer barrageCount;
    private Integer commentCount;
    private List<Tag> tags;
    private List<QualityOptionVO> qualities;

    // 当前用户的互动状态
    private Boolean liked;
    private Boolean disliked;
    private Boolean favorited;
    private Boolean followingAuthor;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class QualityOptionVO {
        private String label;
        private String url;
    }
}
