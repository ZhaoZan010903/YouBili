package cn.tedu.youbiliprojectbackend.modules.tag.classification.dao.mapper;

import cn.tedu.youbiliprojectbackend.modules.tag.classification.pojo.entity.Category;
import cn.tedu.youbiliprojectbackend.modules.tag.classification.pojo.vo.CategoryTitleVO;
import cn.tedu.youbiliprojectbackend.modules.tag.classification.pojo.vo.CategoryVideoVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * 类别mapper接口
 *
 * @author 万佳兴
 */
@Repository
public interface CategoryMapper extends BaseMapper<Category> {

    /**
     * 查询视频分类名称
     *
     * @return CategoryTitleVO
     */
    List<CategoryTitleVO> list();


    /**
     * 查询视频分类关联表
     *
     * @return videoID
     */
    List<CategoryVideoVO> select(Long categoryID);
}
