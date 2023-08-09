package cn.tedu.youbiliprojectbackend.modules.user.account.pojo.param;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户登录的参数类
 *
 * @author java@tedu.cn
 * @version 1.0
 */
@Data
public class UserLoginInfoParam implements Serializable {

    /**
     * 用户名
     */
    @NotNull(message = "请提交用户名")
    @Pattern(regexp = "^[a-zA-Z0-9_]{4,14}$", message = "用户名只能包含字母、数字和下划线，且长度为4-14位")
    private String username;

    /**
     * 密码（原文）
     */
    @NotNull(message = "请提交密码")
    @Pattern(regexp = "^.{4,15}$", message = "密码必须是4~15长度的字符组成")
    private String password;

}
