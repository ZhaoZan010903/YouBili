package cn.tedu.youbiliprojectbackend.modules.recommend.mahoutCF.dao.cache.impl;

import cn.tedu.youbiliprojectbackend.modules.recommend.mahoutCF.dao.cache.DataMahoutCacheCF;
import cn.tedu.youbiliprojectbackend.modules.recommend.mahoutCF.pojo.UserArticleOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

@Repository
public class DataMahoutCacheCFImpl implements DataMahoutCacheCF {
    @Autowired
    RedisTemplate<String, Serializable> redisTemplate;

    /**
     * 查找数据
     *
     * @return 返回数据集合
     */
    @Override
    public List<UserArticleOperation> selectData() {
        ValueOperations<String, Serializable> operations = redisTemplate.opsForValue();
        List<UserArticleOperation> serializable = (List<UserArticleOperation>) operations.get(MAHOUT_DATA);

        return serializable;
    }
}
