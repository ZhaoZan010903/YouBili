package com.youbili.modules.interaction.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@TableName("history")
public class WatchHistory {

    @TableId(value = "historyID", type = IdType.AUTO)
    private Long historyId;

    @TableField("userID")
    private Long userId;

    @TableField("videoID")
    private Long videoId;

    @TableField("watchDate")
    private LocalDateTime watchDate;

    @TableField("watchDuration")
    private LocalTime watchDuration;

    @TableField("enable")
    private Integer enable;

    @TableField(value = "gmtCreate", fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    @TableField(value = "gmtModified", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;
}
