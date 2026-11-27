package com.youbili.modules.video.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("barrage")
public class BarrageRecord {

    @TableId(value = "barrageID", type = IdType.AUTO)
    private Long barrageId;

    @TableField("userID")
    private Long userId;

    @TableField("videoID")
    private Long videoId;

    @TableField("enable")
    private Integer enable;

    @TableField("barrageText")
    private String barrageText;

    @TableField("barrageTime")
    private Float barrageTime;

    @TableField("color")
    private String color;

    @TableField("type")
    private String type;

    @TableField(value = "gmtCreate", fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;
}
