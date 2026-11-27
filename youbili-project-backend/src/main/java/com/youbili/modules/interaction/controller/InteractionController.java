package com.youbili.modules.interaction.controller;

import com.youbili.common.response.ApiResponse;
import com.youbili.common.security.CurrentPrincipal;
import com.youbili.modules.interaction.dto.RecordHistoryRequest;
import com.youbili.modules.interaction.service.FavoriteService;
import com.youbili.modules.interaction.service.HistoryService;
import com.youbili.modules.interaction.service.LikeService;
import com.youbili.modules.interaction.vo.FavoriteItemVO;
import com.youbili.modules.interaction.vo.HistoryItemVO;
import com.youbili.modules.interaction.vo.ReactionVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "视频互动（点赞/收藏/历史）")
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class InteractionController {

    private final LikeService likeService;
    private final FavoriteService favoriteService;
    private final HistoryService historyService;

    @Operation(summary = "查询当前用户对视频的互动状态")
    @GetMapping("/videos/{videoId}/reaction")
    public ApiResponse<ReactionVO> getReaction(@PathVariable Long videoId,
                                               @AuthenticationPrincipal CurrentPrincipal principal) {
        return ApiResponse.success(likeService.getReaction(principal.getUserId(), videoId));
    }

    @Operation(summary = "点赞视频")
    @PostMapping("/videos/{videoId}/like")
    public ApiResponse<ReactionVO> like(@PathVariable Long videoId,
                                        @AuthenticationPrincipal CurrentPrincipal principal) {
        return ApiResponse.success("点赞成功", likeService.like(principal.getUserId(), videoId));
    }

    @Operation(summary = "取消点赞")
    @DeleteMapping("/videos/{videoId}/like")
    public ApiResponse<ReactionVO> cancelLike(@PathVariable Long videoId,
                                              @AuthenticationPrincipal CurrentPrincipal principal) {
        return ApiResponse.success("已取消点赞", likeService.cancelLike(principal.getUserId(), videoId));
    }

    @Operation(summary = "点踩视频")
    @PostMapping("/videos/{videoId}/dislike")
    public ApiResponse<ReactionVO> dislike(@PathVariable Long videoId,
                                           @AuthenticationPrincipal CurrentPrincipal principal) {
        return ApiResponse.success("已点踩", likeService.dislike(principal.getUserId(), videoId));
    }

    @Operation(summary = "取消点踩")
    @DeleteMapping("/videos/{videoId}/dislike")
    public ApiResponse<ReactionVO> cancelDislike(@PathVariable Long videoId,
                                                 @AuthenticationPrincipal CurrentPrincipal principal) {
        return ApiResponse.success("已取消点踩", likeService.cancelDislike(principal.getUserId(), videoId));
    }

    @Operation(summary = "获取收藏列表")
    @GetMapping("/favorites")
    public ApiResponse<List<FavoriteItemVO>> listFavorites(@AuthenticationPrincipal CurrentPrincipal principal,
                                                           @RequestParam(required = false) String keyword) {
        return ApiResponse.success(favoriteService.listFavorites(principal.getUserId(), keyword));
    }

    @Operation(summary = "收藏视频")
    @PostMapping("/favorites/{videoId}")
    public ApiResponse<ReactionVO> addFavorite(@PathVariable Long videoId,
                                               @AuthenticationPrincipal CurrentPrincipal principal) {
        return ApiResponse.success("收藏成功", favoriteService.addFavorite(principal.getUserId(), videoId));
    }

    @Operation(summary = "按视频ID取消收藏")
    @DeleteMapping("/favorites/{videoId}")
    public ApiResponse<ReactionVO> removeFavoriteByVideo(@PathVariable Long videoId,
                                                         @AuthenticationPrincipal CurrentPrincipal principal) {
        return ApiResponse.success("已取消收藏", favoriteService.removeFavoriteByVideo(principal.getUserId(), videoId));
    }

    @Operation(summary = "按收藏记录ID删除收藏")
    @DeleteMapping("/favorites/item/{favoriteId}")
    public ApiResponse<Void> removeFavoriteById(@PathVariable Long favoriteId,
                                                @AuthenticationPrincipal CurrentPrincipal principal) {
        favoriteService.removeFavoriteById(principal.getUserId(), favoriteId);
        return ApiResponse.success("已移出收藏夹", null);
    }

    @Operation(summary = "获取观看历史")
    @GetMapping("/history")
    public ApiResponse<List<HistoryItemVO>> listHistory(@AuthenticationPrincipal CurrentPrincipal principal,
                                                        @RequestParam(required = false) String keyword) {
        return ApiResponse.success(historyService.listHistory(principal.getUserId(), keyword));
    }

    @Operation(summary = "上报观看历史")
    @PostMapping("/history")
    public ApiResponse<Void> recordWatch(@AuthenticationPrincipal CurrentPrincipal principal,
                                         @Valid @RequestBody RecordHistoryRequest request) {
        historyService.recordWatch(principal.getUserId(), request);
        return ApiResponse.success();
    }

    @Operation(summary = "删除单条观看历史")
    @DeleteMapping("/history/{historyId}")
    public ApiResponse<Void> deleteHistory(@PathVariable Long historyId,
                                           @AuthenticationPrincipal CurrentPrincipal principal) {
        historyService.deleteOne(principal.getUserId(), historyId);
        return ApiResponse.success("历史记录已删除", null);
    }

    @Operation(summary = "清空全部观看历史")
    @DeleteMapping("/history")
    public ApiResponse<Void> clearHistory(@AuthenticationPrincipal CurrentPrincipal principal) {
        historyService.clearAll(principal.getUserId());
        return ApiResponse.success("已清空历史记录", null);
    }
}
