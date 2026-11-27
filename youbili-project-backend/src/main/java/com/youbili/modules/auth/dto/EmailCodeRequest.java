package com.youbili.modules.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EmailCodeRequest {

    @NotBlank(message = "请输入邮箱地址")
    @Email(message = "邮箱格式不正确")
    private String email;
}
