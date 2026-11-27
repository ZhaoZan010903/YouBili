package com.youbili.modules.interaction.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("likes")
public class VideoLike {

    @TableId(value = "likeID", type = IdType.AUTO)
    private Long likeId;

    @TableField("userID")
    private Long userId;

    @TableField("videoID")
    private Long videoId;

    @TableField("likeDate")
    private LocalDateTime likeDate;

    @TableField("enable")
    private Integer enable;

    /**
     * 1 = 点赞, 0 = 点踩
     */
    @TableField("typeEnable")
    private Integer typeEnable;

    @TableField(value = "gmtCreate", fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    @TableField(value = "gmtModified", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;
}
