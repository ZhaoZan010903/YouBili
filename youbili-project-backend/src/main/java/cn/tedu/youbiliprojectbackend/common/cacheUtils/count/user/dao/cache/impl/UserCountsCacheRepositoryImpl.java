package cn.tedu.youbiliprojectbackend.common.cacheUtils.count.user.dao.cache.impl;

import cn.tedu.youbiliprojectbackend.common.cacheUtils.count.user.dao.cache.IUserCountsCacheRepository;
import cn.tedu.youbiliprojectbackend.common.cacheUtils.count.user.pojo.vo.UserCountsCacheVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import static cn.tedu.youbiliprojectbackend.common.consts.CountConsts.USER_COUNT;

@Slf4j
@Repository
public class UserCountsCacheRepositoryImpl implements IUserCountsCacheRepository {
    @Autowired
    RedisTemplate<String, Serializable> template;

    boolean status = true;

    @Override
    public void save(List<UserCountsCacheVO> userCounts) {

        ValueOperations<String, Serializable> operations = template.opsForValue();
        for (UserCountsCacheVO userCount : userCounts) {
            operations.set(USER_COUNT + userCount.getUserID() + ":" + status, userCount);
        }
    }

    @Override
    public void deleteAll() {
        log.debug("清空缓存");
        Set<String> keys = template.keys(USER_COUNT + "*" + ":" + "*");
        if (keys != null && !keys.isEmpty()) {
            template.delete(keys);
        }
    }
}
