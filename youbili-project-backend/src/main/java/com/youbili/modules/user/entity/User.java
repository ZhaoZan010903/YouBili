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
@TableName("`user`")
public class User {

    @TableId(value = "userID", type = IdType.AUTO)
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

    @TableField("accountLevel")
    private Integer accountLevel;

    @TableField("fans")
    private Integer fans;

    @TableField("follows")
    private Integer follows;

    @TableField("videoCount")
    private Integer videoCount;

    @TableField("totalLikes")
    private Integer totalLikes;

    @TableField("membershipStatus")
    private String membershipStatus;

    @TableField("registrationDate")
    private LocalDateTime registrationDate;

    @TableField("lastLoginDate")
    private LocalDateTime lastLoginDate;

    @TableField("imgUrl")
    private String imgUrl;

    @TableField("enable")
    private Integer enable;

    @TableField(value = "gmtCreate", fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    @TableField(value = "gmtModified", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;
}
