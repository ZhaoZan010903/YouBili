package com.youbili.modules.recommend.controller;

import com.youbili.common.response.ApiResponse;
import com.youbili.common.security.CurrentPrincipal;
import com.youbili.modules.recommend.service.RecommendService;
import com.youbili.modules.video.vo.VideoCardVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "个性化推荐与热门排行")
@RestController
@RequestMapping("/api/v1/recommend")
@RequiredArgsConstructor
public class RecommendController {

    private final RecommendService recommendService;

    @Operation(summary = "首页个性化推荐流（User-CF + 热门冷启动兜底）")
    @GetMapping("/feed")
    public ApiResponse<List<VideoCardVO>> feed(@AuthenticationPrincipal CurrentPrincipal principal,
                                               @RequestParam(defaultValue = "12") int size) {
        Long userId = principal == null ? null : principal.getUserId();
        return ApiResponse.success(recommendService.getPersonalizedFeed(userId, size));
    }

    @Operation(summary = "首页顶部轮播推荐")
    @GetMapping("/carousel")
    public ApiResponse<List<VideoCardVO>> carousel(@RequestParam(defaultValue = "5") int size) {
        return ApiResponse.success(recommendService.getCarousel(size));
    }

    @Operation(summary = "视频详情页相关推荐")
    @GetMapping("/related")
    public ApiResponse<List<VideoCardVO>> related(@RequestParam(required = false) Long videoId,
                                                  @AuthenticationPrincipal CurrentPrincipal principal,
                                                  @RequestParam(defaultValue = "8") int size) {
        Long userId = principal == null ? null : principal.getUserId();
        return ApiResponse.success(recommendService.getRelatedVideos(videoId, userId, size));
    }

    @Operation(summary = "全站热门排行榜（重力衰减算法）")
    @GetMapping("/hot")
    public ApiResponse<List<VideoCardVO>> hot(@RequestParam(defaultValue = "20") int size) {
        return ApiResponse.success(recommendService.getHotVideos(size));
    }
}
