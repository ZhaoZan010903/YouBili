package com.youbili.modules.video.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("barrages")
public class BarrageRecord {

    @TableId(value = "barrage_id", type = IdType.AUTO)
    private Long barrageId;

    @TableField("user_id")
    private Long userId;

    @TableField("video_id")
    private Long videoId;

    @TableField("enable")
    private Integer enable;

    @TableField("barrage_text")
    private String barrageText;

    @TableField("barrage_time")
    private Float barrageTime;

    @TableField("color")
    private String color;

    @TableField("type")
    private String type;

    @TableField(value = "gmt_create", fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;
}
