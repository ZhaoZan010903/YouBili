package com.youbili.modules.social.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentReplyVO {

    private Long replyId;
    private Long commentId;
    private Long parentReplyId;
    private Long userId;
    private String nickname;
    private String avatarUrl;
    private String replyToNickname;
    private String replyText;
    private Integer likeCount;
    private Integer dislikeCount;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtCreate;
}
