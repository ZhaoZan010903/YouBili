package cn.tedu.youbiliprojectbackend.common.cacheUtils.classification.service;

/**
 * 分类
 *
 * @author 赵錾
 */

public interface ClassificationService {
    /**
     * 启动时将分类列表放入redis之中
     */
    void CacheClassification();
}
