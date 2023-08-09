package cn.tedu.youbiliprojectbackend.modules.user.register.dao.cache;

import cn.tedu.youbiliprojectbackend.common.consts.RegisterConsts;

public interface RegisteCacheRepository extends RegisterConsts {

    /**
     * 发送验证码功能;
     * 将验证码的内容存储到redis之中,其中key为SessionID+email,value为code验证码
     *
     * @param key  sessionID+email组合拼接
     * @param code 验证码
     */
    void AddSendToEmail(String key, String code);

    /**
     * 查询key是否已存在与Redis之中;
     * 存在返回true,不存在返回false
     *
     * @param key Redis之中存储的key
     * @return key存在返回true 不存在返回false
     */
    boolean keyByKey(String key);

    /**
     * 通过key查询这个key的剩余时间
     *
     * @param key Redis之中存储的Key
     * @return key存在返回true 不存在返回false
     */
    Long timeOrdata(String key);


    /**
     * 通过key取出value的值;
     *
     * @param key 存储到Redis之中的key
     * @return 将value返回;
     */
    String valueByKey(String key);
}
