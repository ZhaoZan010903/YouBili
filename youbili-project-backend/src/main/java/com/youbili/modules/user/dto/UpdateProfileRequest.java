package com.youbili.modules.user.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateProfileRequest {

    @Size(min = 1, max = 32, message = "昵称长度需在 1 到 32 个字符之间")
    private String nickname;

    private LocalDate birthday;

    @Size(max = 512, message = "头像地址长度不能超过 512")
    private String avatarUrl;
}
