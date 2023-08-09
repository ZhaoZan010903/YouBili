package cn.tedu.youbiliprojectbackend.common.cacheUtils.classification.dao.cache.impl;

import cn.tedu.youbiliprojectbackend.common.cacheUtils.classification.dao.cache.ClassificationCache;
import cn.tedu.youbiliprojectbackend.modules.tag.classification.pojo.vo.CategoryTitleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

/**
 * 实现类
 *
 * @author 赵錾
 */
@Repository
public class ClssificationCacheImpl implements ClassificationCache {

    @Autowired
    RedisTemplate<String, Serializable> template;

    /**
     * 将分类信息放入缓存之中;
     */
    @Override
    public void SaveClssificationCache(List<CategoryTitleVO> categoryTitleVOS) {
        ValueOperations<String, Serializable> operations = template.opsForValue();
        String key = CATEGORY_LIST;
        operations.set(key, (Serializable) categoryTitleVOS);
    }

    @Override
    public void deleteAll() {
        String key = CATEGORY_LIST;
        template.delete(key);
    }
}
