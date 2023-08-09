package cn.tedu.youbiliprojectbackend.common.cacheUtils.mahout.dao.cache.impl;

import cn.tedu.youbiliprojectbackend.common.cacheUtils.mahout.dao.cache.UserCFRun;
import cn.tedu.youbiliprojectbackend.modules.recommend.mahoutCF.pojo.UserArticleOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

@Repository
@Slf4j
public class UserCFRunImpl implements UserCFRun {


    @Autowired
    RedisTemplate<String, Serializable> redisTemplate;

    /**
     * 将查询到的所有内容存入Redis之中
     */
    @Override
    public void select(List<UserArticleOperation> list) {
        log.debug("写入缓存");
        ValueOperations<String, Serializable> operations = redisTemplate.opsForValue();
        operations.set(MAHOUT_DATA, (Serializable) list);
    }


    /**
     * 清空查询内容
     */
    @Override
    public void delete() {
        log.debug("开始清理数据");
        redisTemplate.delete(MAHOUT_DATA);
    }
}
