package cn.tedu.youbiliprojectbackend.modules.user.account.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("user")
public class Account implements Serializable {
    private Long userID;
    private String username;
    private String nickname;
    private String password;
    private String email;
    private Date birthdaty;
    private int accountLevel;
    private int fans;
    private int follows;
    private int videoCount;
    private int totalLikes;
    private Integer enable;
    private Date registrationDate;
    private Date lastLoginDate;
    private String imgUrl;
    private Date gmtCreate;
    private Date gmtModified;

    private enum MembershipStatus {
        ACTIVE,
        INACTIVE
    }
}
