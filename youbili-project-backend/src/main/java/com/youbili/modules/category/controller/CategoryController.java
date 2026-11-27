package com.youbili.modules.category.controller;

import com.youbili.common.response.ApiResponse;
import com.youbili.common.response.PageResult;
import com.youbili.modules.category.entity.Category;
import com.youbili.modules.category.entity.Tag;
import com.youbili.modules.category.service.CategoryService;
import com.youbili.modules.video.vo.VideoCardVO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@io.swagger.v3.oas.annotations.tags.Tag(name = "分区与标签")
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "获取全部视频分区")
    @GetMapping("/categories")
    public ApiResponse<List<Category>> listCategories() {
        return ApiResponse.success(categoryService.listCategories());
    }

    @Operation(summary = "分页获取分区下的视频")
    @GetMapping("/categories/{categoryId}/videos")
    public ApiResponse<PageResult<VideoCardVO>> listCategoryVideos(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "16") int size,
            @RequestParam(defaultValue = "latest") String sort) {
        return ApiResponse.success(categoryService.listVideosByCategory(categoryId, page, size, sort));
    }

    @Operation(summary = "获取全部标签列表")
    @GetMapping("/tags")
    public ApiResponse<List<Tag>> listTags() {
        return ApiResponse.success(categoryService.listTags());
    }
}
