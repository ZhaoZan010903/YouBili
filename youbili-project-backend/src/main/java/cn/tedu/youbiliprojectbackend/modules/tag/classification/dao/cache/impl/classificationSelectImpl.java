package cn.tedu.youbiliprojectbackend.modules.tag.classification.dao.cache.impl;

import cn.tedu.youbiliprojectbackend.modules.tag.classification.dao.cache.classificationSelect;
import cn.tedu.youbiliprojectbackend.modules.tag.classification.pojo.vo.CategoryTitleVO;
import cn.tedu.youbiliprojectbackend.modules.tag.classification.pojo.vo.CategoryVideoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

/**
 * @author 赵錾
 */
@Repository
public class classificationSelectImpl implements classificationSelect {

    @Autowired
    RedisTemplate<String, Serializable> redisTemplate;


    /**
     * 从缓存之中查询出分类列表内容
     *
     * @return 分类列表
     */
    @Override
    public List<CategoryTitleVO> list() {
        ValueOperations<String, Serializable> operations = redisTemplate.opsForValue();
        String key = CATEGORY_LIST;
        List<CategoryTitleVO> categoryTitleVOS = (List<CategoryTitleVO>) operations.get(key);
        return categoryTitleVOS;
    }

    @Override
    public List<CategoryVideoVO> listVideo(Long categoryID, Integer pageNum, Integer pageSize) {
        return null;
    }
}
