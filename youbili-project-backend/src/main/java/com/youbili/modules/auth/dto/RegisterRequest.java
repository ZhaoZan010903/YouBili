package com.youbili.modules.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "请输入用户名")
    @Pattern(regexp = "^[a-zA-Z0-9_]{4,24}$", message = "用户名需为 4-24 位字母、数字或下划线")
    private String username;

    @NotBlank(message = "请输入密码")
    @Size(min = 6, max = 32, message = "密码长度需在 6-32 位之间")
    private String password;

    @NotBlank(message = "请输入昵称")
    @Size(min = 1, max = 32, message = "昵称长度需在 1-32 位之间")
    private String nickname;

    @NotBlank(message = "请输入邮箱")
    @Email(message = "邮箱格式不正确")
    private String email;

    @NotBlank(message = "请输入邮箱验证码")
    @Pattern(regexp = "^\\d{6}$", message = "验证码需为 6 位数字")
    private String code;
}
