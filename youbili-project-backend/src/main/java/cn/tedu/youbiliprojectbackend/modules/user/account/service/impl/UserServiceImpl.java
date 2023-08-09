package cn.tedu.youbiliprojectbackend.modules.user.account.service.impl;

import cn.tedu.youbiliprojectbackend.common.pojo.po.UserLoginInfoPO;
import cn.tedu.youbiliprojectbackend.common.utils.jwt.JwtUtils;
import cn.tedu.youbiliprojectbackend.modules.user.account.dao.cache.impl.UserCacheRepository;
import cn.tedu.youbiliprojectbackend.modules.user.account.dao.mapper.AccountMapper;
import cn.tedu.youbiliprojectbackend.modules.user.account.pojo.param.UserLoginInfoParam;
import cn.tedu.youbiliprojectbackend.modules.user.account.pojo.vo.AccountMeVO;
import cn.tedu.youbiliprojectbackend.modules.user.account.pojo.vo.UserLoginVO;
import cn.tedu.youbiliprojectbackend.modules.user.account.security.CustomUserDetails;
import cn.tedu.youbiliprojectbackend.modules.user.account.service.IUserService;
import cn.tedu.youbiliprojectbackend.modules.user.register.dao.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    UserCacheRepository userCacheRepository;
    @Autowired
    UserMapper userMapper;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private AccountMapper accountMapper;

    @Override
    public UserLoginVO login(UserLoginInfoParam userLoginInfoParam, String remoteAddr, String userAgent) {

        log.debug("开始处理【用户登录】的业务，参数：{}", userLoginInfoParam);
        // 验证 !
        Authentication authentication = new UsernamePasswordAuthenticationToken( //验证用户名密码
                userLoginInfoParam.getUsername(), userLoginInfoParam.getPassword());
        log.debug("准备调用AuthenticationManager的认证方法，判断此用户名、密码是否可以成功登录……");
        Authentication authenticateResult = authenticationManager.authenticate(authentication);
        log.debug("验证用户登录成功，返回的认证结果：{}", authenticateResult);

        Object principal = authenticateResult.getPrincipal();
        log.debug("从认证结果中获取当事人：{}", principal);
        CustomUserDetails userDetails = (CustomUserDetails) principal;

        String Token = JwtUtils.createJWT(userDetails); //JWT工具类

        UserLoginInfoPO loginInfoPO = new UserLoginInfoPO()
                .setUserAgent(userAgent).setIp(remoteAddr);

        //将登录JWT,IP和浏览器信息存储到Redis之中;
        userCacheRepository.saveLoginInfo(Token, loginInfoPO);
        log.debug("生成的jwt{}", Token);
        //将用户状态存入Redis之中;
        userCacheRepository.saveEnableByUserID(userDetails.getUserID(), 1);


        // 用户登录之后将token， 用户名， 头像返回给前台
        UserLoginVO userLoginVO = new UserLoginVO().setToken(Token);
        userLoginVO.setUsername(userDetails.getUsername());
        userLoginVO.setImgUrl(userDetails.getImgUrl());


        return userLoginVO;
    }


    @Override
    public AccountMeVO me(Long userID) {
        return accountMapper.SelectByMe(userID);
    }
}
