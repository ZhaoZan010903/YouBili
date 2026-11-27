package com.youbili.modules.social.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("notices")
public class Notice {

    @TableId(value = "notice_id", type = IdType.AUTO)
    private Long noticeId;

    @TableField("user_id")
    private Long userId;

    @TableField("system_name")
    private String systemName;

    /**
     * 0 = 系统公告, 1 = 个人消息
     */
    @TableField("type_enable")
    private Integer typeEnable;

    @TableField("is_read")
    private Integer isRead;

    @TableField("notice_text")
    private String noticeText;

    @TableField("notice_time")
    private LocalDateTime noticeTime;

    @TableField(value = "gmt_create", fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    @TableField(value = "gmt_modified", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;
}
