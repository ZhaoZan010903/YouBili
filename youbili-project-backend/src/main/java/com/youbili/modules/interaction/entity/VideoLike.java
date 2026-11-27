package com.youbili.modules.interaction.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("video_likes")
public class VideoLike {

    @TableId(value = "like_id", type = IdType.AUTO)
    private Long likeId;

    @TableField("user_id")
    private Long userId;

    @TableField("video_id")
    private Long videoId;

    @TableField("like_date")
    private LocalDateTime likeDate;

    @TableField("enable")
    private Integer enable;

    /**
     * 1 = 点赞, 0 = 点踩
     */
    @TableField("type_enable")
    private Integer typeEnable;

    @TableField(value = "gmt_create", fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    @TableField(value = "gmt_modified", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;
}
