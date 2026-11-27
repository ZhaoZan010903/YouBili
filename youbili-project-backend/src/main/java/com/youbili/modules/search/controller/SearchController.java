package com.youbili.modules.search.controller;

import com.youbili.common.response.ApiResponse;
import com.youbili.common.response.PageResult;
import com.youbili.modules.search.service.VideoSearchService;
import com.youbili.modules.video.vo.VideoCardVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "视频搜索")
@RestController
@RequestMapping("/api/v1/search")
@RequiredArgsConstructor
public class SearchController {

    private final VideoSearchService videoSearchService;

    @Operation(summary = "按关键词搜索视频（ES 优先，自动降级 PostgreSQL）")
    @GetMapping("/videos")
    public ApiResponse<PageResult<VideoCardVO>> searchVideos(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "16") int size,
            @RequestParam(defaultValue = "latest") String sort) {
        return ApiResponse.success(videoSearchService.search(keyword, page, size, sort));
    }

    @Operation(summary = "搜索框实时联想建议")
    @GetMapping("/suggest")
    public ApiResponse<List<String>> suggest(@RequestParam(defaultValue = "") String keyword) {
        return ApiResponse.success(videoSearchService.suggest(keyword));
    }
}
