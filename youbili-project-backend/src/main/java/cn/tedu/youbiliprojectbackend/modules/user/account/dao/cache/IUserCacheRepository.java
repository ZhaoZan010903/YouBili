package cn.tedu.youbiliprojectbackend.modules.user.account.dao.cache;

import cn.tedu.youbiliprojectbackend.common.consts.UserCacheConsts;
import cn.tedu.youbiliprojectbackend.common.pojo.po.UserLoginInfoPO;

public interface IUserCacheRepository extends UserCacheConsts {

    /**
     * \
     * 保存用户登录信息到Redis中;
     *
     * @param Token           JWT工具类生成的Token令牌
     * @param userLoginInfoPO 用户登录信息;包括IP,浏览器信息
     */
    void saveLoginInfo(String Token, UserLoginInfoPO userLoginInfoPO);

    /**
     * 根据JWT从redis之中取出用户的登录信息
     *
     * @param Token JWT
     * @return 返回用户登录的信息
     */
    UserLoginInfoPO getLoginStatus(String Token);

    /**
     * 保存用户的账号状态进redis中
     *
     * @param userID 用户ID
     * @param enable 账号状态: 1表示启用,0表示禁用
     */
    void saveEnableByUserID(Long userID, int enable);


    /**
     * 通过用户ID取出用户状态
     *
     * @param userID 用户ID
     * @return 返回值为true时, 账号被封禁;为false时,账号开启;
     */
    boolean getEnableByUserID(Long userID);
}
