package cn.tedu.youbiliprojectbackend.modules.tag.classification.service.impl;

import cn.tedu.youbiliprojectbackend.common.cacheUtils.videoList.pojo.vo.VideoUserCache;
import cn.tedu.youbiliprojectbackend.common.consts.VideoConsts;
import cn.tedu.youbiliprojectbackend.modules.tag.classification.dao.cache.classificationSelect;
import cn.tedu.youbiliprojectbackend.modules.tag.classification.dao.repository.ICategoryRepository;
import cn.tedu.youbiliprojectbackend.modules.tag.classification.pojo.vo.CategoryTitleVO;
import cn.tedu.youbiliprojectbackend.modules.tag.classification.pojo.vo.CategoryVideoVO;
import cn.tedu.youbiliprojectbackend.modules.tag.classification.service.ICategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 类别的service接口的实现类
 *
 * @author 万佳兴
 */
@Slf4j
@Service
public class CategoryServiceImpl implements ICategoryService, VideoConsts {

    @Autowired
    RedisTemplate<String, Serializable> redisTemplate;
    @Autowired
    classificationSelect classificationSelect;
    @Autowired
    private ICategoryRepository categoryRepository;

    public CategoryServiceImpl() {
        log.info("创建业务层对象：CategoryServiceImpl");
    }

    @Override
    public List<CategoryTitleVO> listTile() {
        List<CategoryTitleVO> list = classificationSelect.list();
        return list;
    }

    @Override
    public List<VideoUserCache> selectVideo(Long categoryID) {

        List<CategoryVideoVO> select = categoryRepository.select(categoryID);
        log.debug("查询视频ID: {}", select);
        ValueOperations<String, Serializable> opsForValue = redisTemplate.opsForValue();
        List<VideoUserCache> categoryListVOS = new ArrayList<>();
        for (CategoryVideoVO categoryVideoVO : select) {
            Set<String> keys = redisTemplate.keys(VIDEO_LIST + "*:" + categoryVideoVO.getVideoID());
            for (String key : keys) {
                VideoUserCache serializable = (VideoUserCache) opsForValue.get(key);
                categoryListVOS.add(serializable);
            }
        }
        log.debug(String.valueOf(categoryListVOS));
        return categoryListVOS;
    }
}
