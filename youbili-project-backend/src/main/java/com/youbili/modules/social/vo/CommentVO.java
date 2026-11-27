package com.youbili.modules.social.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Data
public class CommentVO {

    private Long commentId;
    private Long userId;
    private String nickname;
    private String avatarUrl;
    private String commentText;
    private Integer likeCount;
    private Integer dislikeCount;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtCreate;

    private Integer replyCount = 0;
    private List<CommentReplyVO> replies = Collections.emptyList();
}
