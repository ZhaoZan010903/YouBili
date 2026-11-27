package com.youbili.modules.user.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("follows")
public class Follow {

    @TableId(value = "follow_id", type = IdType.AUTO)
    private Long followId;

    @TableField("follower_id")
    private Long followerId;

    @TableField("followed_id")
    private Long followedId;

    @TableField("follow_date")
    private LocalDateTime followDate;

    @TableField("enable")
    private Integer enable;

    @TableField(value = "gmt_create", fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    @TableField(value = "gmt_modified", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;
}
