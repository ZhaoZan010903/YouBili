package com.youbili.modules.category.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.youbili.common.response.PageResult;
import com.youbili.modules.category.entity.Category;
import com.youbili.modules.category.entity.Tag;
import com.youbili.modules.category.mapper.CategoryMapper;
import com.youbili.modules.category.mapper.TagMapper;
import com.youbili.modules.video.mapper.VideoMapper;
import com.youbili.modules.video.vo.VideoCardVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryMapper categoryMapper;
    private final TagMapper tagMapper;
    private final VideoMapper videoMapper;

    public List<Category> listCategories() {
        return categoryMapper.selectList(
                new LambdaQueryWrapper<Category>()
                        .eq(Category::getEnable, 1)
                        .orderByAsc(Category::getCategoryId)
        );
    }

    public List<Tag> listTags() {
        return tagMapper.selectList(
                new LambdaQueryWrapper<Tag>()
                        .eq(Tag::getEnable, 1)
                        .orderByAsc(Tag::getTagId)
        );
    }

    public PageResult<VideoCardVO> listVideosByCategory(Long categoryId, int page, int size, String sort) {
        int safePage = Math.max(page, 1);
        int safeSize = Math.min(Math.max(size, 1), 60);
        IPage<VideoCardVO> result = videoMapper.selectCategoryVideos(
                new Page<>(safePage, safeSize), categoryId, sort
        );
        return PageResult.of(result.getCurrent(), result.getSize(), result.getTotal(), result.getRecords());
    }
}
