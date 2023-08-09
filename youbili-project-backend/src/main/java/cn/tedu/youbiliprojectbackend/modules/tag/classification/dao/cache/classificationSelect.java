package cn.tedu.youbiliprojectbackend.modules.tag.classification.dao.cache;

import cn.tedu.youbiliprojectbackend.common.consts.ClssificationConsts;
import cn.tedu.youbiliprojectbackend.modules.tag.classification.pojo.vo.CategoryTitleVO;
import cn.tedu.youbiliprojectbackend.modules.tag.classification.pojo.vo.CategoryVideoVO;

import java.util.List;

/**
 * 从缓存之中查询出数据信息
 *
 * @author 赵錾
 */
public interface classificationSelect extends ClssificationConsts {
    /**
     * 从缓存之中查询出分类列表内容
     *
     * @return 分类列表
     */
    List<CategoryTitleVO> list();

    /**
     * 从缓存中查询出对应分类的视频
     */
    List<CategoryVideoVO> listVideo(Long categoryID, Integer pageNum, Integer pageSize);
}

