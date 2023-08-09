package cn.tedu.youbiliprojectbackend.modules.tag.classification.dao.repository;


import cn.tedu.youbiliprojectbackend.modules.tag.classification.pojo.vo.CategoryTitleVO;
import cn.tedu.youbiliprojectbackend.modules.tag.classification.pojo.vo.CategoryVideoVO;

import java.util.List;

/**
 * 类别的repository接口
 *
 * @author 万佳兴
 */
public interface ICategoryRepository {

    /**
     * 查询视频分类名称
     *
     * @return CategoryTitleVO
     */
    List<CategoryTitleVO> list();

    /**
     * 查询视频分类的ID
     */
    List<CategoryVideoVO> select(Long categoryID);
}
