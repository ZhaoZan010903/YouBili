package cn.tedu.youbiliprojectbackend.modules.user.register.pojo.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserRegist implements Serializable {
    private Long userID;
    private String username;

}
