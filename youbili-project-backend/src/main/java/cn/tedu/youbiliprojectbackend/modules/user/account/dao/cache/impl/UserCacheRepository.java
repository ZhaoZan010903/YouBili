package cn.tedu.youbiliprojectbackend.modules.user.account.dao.cache.impl;

import cn.tedu.youbiliprojectbackend.common.pojo.po.UserLoginInfoPO;
import cn.tedu.youbiliprojectbackend.modules.user.account.dao.cache.IUserCacheRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

@Slf4j
@Repository
public class UserCacheRepository implements IUserCacheRepository {


    @Autowired
    RedisTemplate<String, Serializable> redisTemplate;


    /**
     * 保存用户登录信息到Redis中,其中KEY为"user:jwt: 'Token:'",value为userLoginInfoPO;
     *
     * @param Token           JWT工具类生成的Token令牌
     * @param userLoginInfoPO 用户登录信息;包括IP,浏览器信息
     */
    @Override
    public void saveLoginInfo(String Token, UserLoginInfoPO userLoginInfoPO) {
        String key = USER_JWT_PREFIX + Token;
        log.debug("Token :{}", Token);
        log.debug("key值为:{}", key);
        ValueOperations<String, Serializable> opsForValue = redisTemplate.opsForValue();
        opsForValue.set(key, userLoginInfoPO, 10, TimeUnit.DAYS);
    }


    /**
     * 根据JWT从redis之中取出用户的登录信息
     *
     * @param Token JWT
     * @return 返回用户登录的信息
     */
    @Override
    public UserLoginInfoPO getLoginStatus(String Token) {
        log.debug("开始从Redis中取出用户信息");
        String key = USER_JWT_PREFIX + Token;
        log.debug("key值为:{}", key);
        ValueOperations<String, Serializable> operations = redisTemplate.opsForValue();
        Serializable serializable = operations.get(key);
        UserLoginInfoPO userLoginInfoPO = (UserLoginInfoPO) serializable;
        log.debug("取出的用户信息是:{}", userLoginInfoPO);
        return userLoginInfoPO;
    }

    /**
     * 保存用户的账号状态进redis中
     *
     * @param userID 用户ID
     * @param enable 账号状态: 1表示启用,0表示禁用
     */
    @Override
    public void saveEnableByUserID(Long userID, int enable) {
        String key = USER_ENABLE_PREFIX + userID;
        ValueOperations<String, Serializable> operations = redisTemplate.opsForValue();
        operations.set(key, enable, 10, TimeUnit.DAYS);
    }

    /**
     * 通过用户ID取出用户状态
     *
     * @param userID 用户ID
     * @return 返回值为true时, 账号被封禁;为false时,账号开启;
     */
    @Override
    public boolean getEnableByUserID(Long userID) {
        String key = USER_ENABLE_PREFIX + userID;
        ValueOperations<String, Serializable> operations = redisTemplate.opsForValue();
        operations.get(key);
        return false;
    }
}
