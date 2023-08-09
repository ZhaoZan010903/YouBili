package cn.tedu.youbiliprojectbackend.modules.user.account.service;


import cn.tedu.youbiliprojectbackend.modules.user.account.pojo.param.UserLoginInfoParam;
import cn.tedu.youbiliprojectbackend.modules.user.account.pojo.vo.AccountMeVO;
import cn.tedu.youbiliprojectbackend.modules.user.account.pojo.vo.UserLoginVO;

public interface IUserService {

    /**
     * 登录实习
     *
     * @param userLoginInfoParam 用户名密码表单
     * @param remoteAddr         IP地址
     * @param userAgent          客户端的浏览器信息
     * @return 登录成功后将返回用户名, 用户ID等
     */
    UserLoginVO login(UserLoginInfoParam userLoginInfoParam, String remoteAddr, String userAgent);

    AccountMeVO me(Long userID);

}
