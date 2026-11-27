package com.youbili.modules.social.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("comment_replies")
public class CommentReply {

    @TableId(value = "reply_id", type = IdType.AUTO)
    private Long replyId;

    @TableField("comment_id")
    private Long commentId;

    @TableField("parent_reply_id")
    private Long parentReplyId;

    @TableField("user_id")
    private Long userId;

    @TableField("reply_text")
    private String replyText;

    @TableField("reply_date")
    private LocalDateTime replyDate;

    @TableField("like_count")
    private Integer likeCount;

    @TableField("dislike_count")
    private Integer dislikeCount;

    @TableField("enable")
    private Integer enable;

    @TableField(value = "gmt_create", fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    @TableField(value = "gmt_modified", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;
}
