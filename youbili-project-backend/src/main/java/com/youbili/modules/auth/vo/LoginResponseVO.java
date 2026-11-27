package com.youbili.modules.auth.vo;

import com.youbili.modules.user.vo.UserProfileVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseVO {

    private String token;
    private String tokenType;
    private UserProfileVO user;
}
