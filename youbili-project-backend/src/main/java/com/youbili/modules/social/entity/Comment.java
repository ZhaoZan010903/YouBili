package com.youbili.modules.social.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("comment")
public class Comment {

    @TableId(value = "commentID", type = IdType.AUTO)
    private Long commentId;

    @TableField("userID")
    private Long userId;

    @TableField("videoID")
    private Long videoId;

    @TableField("commentText")
    private String commentText;

    @TableField("commentDate")
    private LocalDateTime commentDate;

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
