package com.youbili.common.security;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.youbili.modules.user.entity.User;
import com.youbili.modules.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (!StringUtils.hasText(username)) {
            throw new UsernameNotFoundException("用户名不能为空");
        }
        User user = userMapper.selectOne(
                new LambdaQueryWrapper<User>().eq(User::getUsername, username.trim())
        );
        if (user == null) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }
        boolean enabled = Integer.valueOf(1).equals(user.getEnable());
        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        return new CustomUserDetails(
                user.getUserId(),
                user.getUsername(),
                user.getPassword(),
                user.getNickname(),
                user.getImgUrl(),
                enabled,
                authorities
        );
    }
}
