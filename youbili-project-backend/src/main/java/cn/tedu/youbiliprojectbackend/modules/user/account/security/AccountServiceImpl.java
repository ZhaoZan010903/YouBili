package cn.tedu.youbiliprojectbackend.modules.user.account.security;

import cn.tedu.youbiliprojectbackend.modules.user.account.dao.mapper.AccountMapper;
import cn.tedu.youbiliprojectbackend.modules.user.account.pojo.entity.Account;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class AccountServiceImpl implements UserDetailsService {

    @Autowired
    AccountMapper accountMapper;

    AccountServiceImpl() {
        log.debug("AccountServiceImpl开始工作");
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.debug("Spring Security框架自动调用了UserDetailsService对象，将根据用户名获取用户详情，参数：{}", username);
         /*
        如果用户名为空或者不存在，就抛出一个 UsernameNotFoundException 异常
         */

        if (username == null || username.isEmpty()) {
            throw new UsernameNotFoundException("用户名不能为空");
        }

        Account account = accountMapper.SelectByUserName(username);
        log.debug("根据用户名【{}】从数据库中查询用户详情，查询结果：{}", username, account);

        if (account == null) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }


        List<GrantedAuthority> authorities = new ArrayList<>(); //权限!


        CustomUserDetails userDetails = new CustomUserDetails(
                account.getUserID(),
                account.getUsername(),
                account.getPassword(),
                account.getImgUrl(),
                account.getEnable() == 1,
                authorities
        );

        log.debug(userDetails.toString());
        log.debug("返回给Security框架,让框架对用户名密码进行判断!");

        return userDetails;


    }
}
