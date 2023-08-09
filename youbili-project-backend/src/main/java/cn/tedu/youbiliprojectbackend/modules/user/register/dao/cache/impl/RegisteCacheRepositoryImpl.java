package cn.tedu.youbiliprojectbackend.modules.user.register.dao.cache.impl;

import cn.tedu.youbiliprojectbackend.modules.user.register.dao.cache.RegisteCacheRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@Repository
public class RegisteCacheRepositoryImpl implements RegisteCacheRepository {

    @Autowired
    RedisTemplate<String, Serializable> redisTemplate;

    /**
     * 发送验证码功能;
     * 将验证码的内容存储到redis之中,其中key为SessionID+email,value为code验证码
     *
     * @param key  sessionID+email组合拼接
     * @param code 验证码
     */
    @Override
    public void AddSendToEmail(String key, String code) {
        log.debug("开始向Redis中添加数据email:{},code:{}", key, code);
        ValueOperations<String, Serializable> operations = redisTemplate.opsForValue();
        operations.set(key, code, 3, TimeUnit.MINUTES);
    }

    /**
     * 查询key是否已存在与Redis之中;
     * 存在返回true,不存在返回false
     *
     * @param key
     * @return
     */
    @Override
    public boolean keyByKey(String key) {
        log.debug("判断Key是否存在,开始查询key是否存在于redis之中");
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    /**
     * 通过key查询这个key的剩余时间
     *
     * @param key Redis之中存储的Key
     * @return key存在返回true 不存在返回false
     */
    @Override
    public Long timeOrdata(String key) {
        log.debug("开始查询剩余时间");
        return Optional.ofNullable(redisTemplate.getExpire(key, TimeUnit.SECONDS)).orElse(0L);
    }

    /**
     * 通过key取出value的值;
     *
     * @param key 存储到Redis之中的key
     * @return 将value返回;
     */
    @Override
    public String valueByKey(String key) {
        log.debug("开始通过key取出value");
        String code = (String) redisTemplate.opsForValue().get(key);
        return code;
    }
}
