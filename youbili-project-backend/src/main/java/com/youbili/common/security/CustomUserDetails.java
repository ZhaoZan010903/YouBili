package com.youbili.common.security;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
@EqualsAndHashCode(callSuper = true)
public class CustomUserDetails extends User {

    private final Long userId;
    private final String nickname;
    private final String avatarUrl;

    public CustomUserDetails(Long userId,
                             String username,
                             String password,
                             String nickname,
                             String avatarUrl,
                             boolean enabled,
                             Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, true, true, true, authorities);
        this.userId = userId;
        this.nickname = nickname;
        this.avatarUrl = avatarUrl;
    }
}
