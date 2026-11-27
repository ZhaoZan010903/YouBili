package com.youbili.modules.social.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("notice")
public class Notice {

    @TableId(value = "noticeID", type = IdType.AUTO)
    private Long noticeId;

    @TableField("userID")
    private Long userId;

    @TableField("systemName")
    private String systemName;

    /**
     * 0 = 系统公告, 1 = 个人消息
     */
    @TableField("typeEnable")
    private Integer typeEnable;

    @TableField("isRead")
    private Integer isRead;

    @TableField("noticeText")
    private String noticeText;

    @TableField("noticeTime")
    private LocalDateTime noticeTime;

    @TableField(value = "gmtCreate", fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    @TableField(value = "gmtModified", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;
}
