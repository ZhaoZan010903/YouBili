package cn.tedu.youbiliprojectbackend.common.cacheUtils.classification.dao.cache;

import cn.tedu.youbiliprojectbackend.common.consts.ClssificationConsts;
import cn.tedu.youbiliprojectbackend.modules.tag.classification.pojo.vo.CategoryTitleVO;

import java.util.List;

/**
 * 分类缓存
 *
 * @author 赵錾
 */
public interface ClassificationCache extends ClssificationConsts {
    /**
     * 将分类信息放入缓存之中;
     */
    void SaveClssificationCache(List<CategoryTitleVO> categoryTitleVOS);

    void deleteAll();
}
