package cn.tedu.youbiliprojectbackend.modules.user.register.pojo.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;

import java.io.Serializable;


@Validated
@Data
public class AccountRegDTO implements Serializable {
    /**
     * 用户名
     * 长度 4 - 14
     * 只能包含字母、数字、下划线或短横线，并且长度为 4 到 14 位
     */
    @Pattern(regexp = "^[a-zA-Z0-9_]{4,14}$", message = "用户名只能包含字母、数字和下划线，且长度为4-14位")
    private String username;
    /**
     * 密码:
     * 长度6-18
     */
    @Length(min = 6, max = 18)
    private String password;

    /**
     * 邮箱:
     * 格式为邮箱
     */
    @Email(message = "您输入的邮箱格式错误,请重新输入!")
    private String email;

    /**
     * 昵称:
     * 长度1-10
     */
    @Length(min = 1, max = 10)
    private String nickname;

    /**
     * 验证码
     */
    @Length(min = 6, max = 6)
    private String code;

}
