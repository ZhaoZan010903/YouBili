package com.youbili.modules.social.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("report")
public class Report {

    @TableId(value = "reportID", type = IdType.AUTO)
    private Long reportId;

    @TableField("userID")
    private Long userId;

    @TableField("targetType")
    private String targetType;

    @TableField("targetID")
    private Long targetId;

    @TableField("category")
    private String category;

    @TableField("reasons")
    private String reasons;

    @TableField("description")
    private String description;

    @TableField("status")
    private String status;

    @TableField(value = "gmtCreate", fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    @TableField(value = "gmtModified", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;
}
