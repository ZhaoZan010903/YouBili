package com.youbili.modules.social.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("commentreplie")
public class CommentReply {

    @TableId(value = "replyID", type = IdType.AUTO)
    private Long replyId;

    @TableField("commentID")
    private Long commentId;

    @TableField("parentReplyID")
    private Long parentReplyId;

    @TableField("userID")
    private Long userId;

    @TableField("replyText")
    private String replyText;

    @TableField("replyDate")
    private LocalDateTime replyDate;

    @TableField("likeCount")
    private Integer likeCount;

    @TableField("dislikeCount")
    private Integer dislikeCount;

    @TableField("enable")
    private Integer enable;

    @TableField(value = "gmtCreate", fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    @TableField(value = "gmtModified", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;
}
