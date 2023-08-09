package cn.tedu.youbiliprojectbackend.common.cacheUtils.classification.service.impl;

import cn.tedu.youbiliprojectbackend.common.cacheUtils.classification.dao.cache.ClassificationCache;
import cn.tedu.youbiliprojectbackend.common.cacheUtils.classification.service.ClassificationService;
import cn.tedu.youbiliprojectbackend.modules.tag.classification.dao.repository.ICategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 实现类
 *
 * @author 赵錾
 */
@Slf4j
@Service
public class ClassificationServiceImpl implements ClassificationService {

    @Autowired
    ClassificationCache classificationCache;
    @Autowired
    private ICategoryRepository categoryRepository;


    /**
     * 启动时将分类列表放入redis之中
     */
    @Override
    public void CacheClassification() {
        log.debug("开始向缓存之中写入数据");
        classificationCache.deleteAll();
        classificationCache.SaveClssificationCache(categoryRepository.list());
    }
}
