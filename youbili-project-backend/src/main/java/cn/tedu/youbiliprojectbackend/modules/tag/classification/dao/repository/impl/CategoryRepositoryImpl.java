package cn.tedu.youbiliprojectbackend.modules.tag.classification.dao.repository.impl;

import cn.tedu.youbiliprojectbackend.modules.tag.classification.dao.mapper.CategoryMapper;
import cn.tedu.youbiliprojectbackend.modules.tag.classification.dao.repository.ICategoryRepository;
import cn.tedu.youbiliprojectbackend.modules.tag.classification.pojo.vo.CategoryTitleVO;
import cn.tedu.youbiliprojectbackend.modules.tag.classification.pojo.vo.CategoryVideoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 类别的repository实现类
 *
 * @author 万佳兴
 */
@Slf4j
@Repository
public class CategoryRepositoryImpl implements ICategoryRepository {

    @Autowired
    private CategoryMapper categoryMapper;

    public CategoryRepositoryImpl() {
        log.info("创建存储库对象：CategoryRepositoryImpl");
    }

    @Override
    public List<CategoryTitleVO> list() {
        return categoryMapper.list();
    }

    @Override
    public List<CategoryVideoVO> select(Long categoryID) {
        List<CategoryVideoVO> select = categoryMapper.select(categoryID);
        return select;
    }
}
