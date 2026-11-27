package com.youbili.modules.user.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("users")
public class User {

    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;

    @TableField("username")
    private String username;

    @TableField("nickname")
    private String nickname;

    @TableField("password")
    private String password;

    @TableField("email")
    private String email;

    @TableField("birthday")
    private LocalDate birthday;

    @TableField("account_level")
    private Integer accountLevel;

    @TableField("fans")
    private Integer fans;

    @TableField("follows")
    private Integer follows;

    @TableField("video_count")
    private Integer videoCount;

    @TableField("total_likes")
    private Integer totalLikes;

    @TableField("membership_status")
    private String membershipStatus;

    @TableField("registration_date")
    private LocalDateTime registrationDate;

    @TableField("last_login_date")
    private LocalDateTime lastLoginDate;

    @TableField("img_url")
    private String imgUrl;

    @TableField("enable")
    private Integer enable;

    @TableField(value = "gmt_create", fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    @TableField(value = "gmt_modified", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;
}
