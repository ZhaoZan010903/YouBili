package com.youbili.modules.video.controller;

import com.youbili.common.response.ApiResponse;
import com.youbili.common.response.PageResult;
import com.youbili.common.security.CurrentPrincipal;
import com.youbili.modules.category.entity.Tag;
import com.youbili.modules.video.dto.PublishVideoRequest;
import com.youbili.modules.video.dto.UpdateVideoRequest;
import com.youbili.modules.video.service.StorageService;
import com.youbili.modules.video.service.VideoService;
import com.youbili.modules.video.vo.UploadResultVO;
import com.youbili.modules.video.vo.VideoCardVO;
import com.youbili.modules.video.vo.VideoDetailVO;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@io.swagger.v3.oas.annotations.tags.Tag(name = "视频与投稿")
@RestController
@RequestMapping("/api/v1/videos")
@RequiredArgsConstructor
public class VideoController {

    private final VideoService videoService;
    private final StorageService storageService;

    @Operation(summary = "获取视频详情")
    @GetMapping("/{videoId}")
    public ApiResponse<VideoDetailVO> detail(@PathVariable Long videoId,
                                             @AuthenticationPrincipal CurrentPrincipal principal) {
        Long userId = principal == null ? null : principal.getUserId();
        return ApiResponse.success(videoService.getVideoDetail(videoId, userId));
    }

    @Operation(summary = "获取视频标签")
    @GetMapping("/{videoId}/tags")
    public ApiResponse<List<Tag>> tags(@PathVariable Long videoId) {
        return ApiResponse.success(videoService.getVideoTags(videoId));
    }

    @Operation(summary = "获取当前用户的投稿列表（稿件管理）")
    @GetMapping("/my")
    public ApiResponse<PageResult<VideoCardVO>> myVideos(
            @AuthenticationPrincipal CurrentPrincipal principal,
            @RequestParam(required = false) String reviewStatus,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.success(videoService.listMyVideos(principal.getUserId(), reviewStatus, page, size));
    }

    @Operation(summary = "上传视频文件")
    @PostMapping("/upload/file")
    public ApiResponse<UploadResultVO> uploadVideo(@AuthenticationPrincipal CurrentPrincipal principal,
                                                   @RequestParam("file") MultipartFile file) {
        return ApiResponse.success("视频上传成功", storageService.storeVideo(principal.getUserId(), file));
    }

    @Operation(summary = "上传视频封面")
    @PostMapping("/upload/cover")
    public ApiResponse<UploadResultVO> uploadCover(@AuthenticationPrincipal CurrentPrincipal principal,
                                                   @RequestParam("file") MultipartFile file) {
        return ApiResponse.success("封面上传成功", storageService.storeCover(principal.getUserId(), file));
    }

    @Operation(summary = "发布视频稿件")
    @PostMapping("/publish")
    public ApiResponse<Map<String, Long>> publish(@AuthenticationPrincipal CurrentPrincipal principal,
                                                  @Valid @RequestBody PublishVideoRequest request) {
        Long videoId = videoService.publishVideo(principal.getUserId(), request);
        return ApiResponse.success("投稿成功", Map.of("videoId", videoId));
    }

    @Operation(summary = "编辑视频稿件")
    @PutMapping("/{videoId}")
    public ApiResponse<Void> update(@PathVariable Long videoId,
                                    @AuthenticationPrincipal CurrentPrincipal principal,
                                    @Valid @RequestBody UpdateVideoRequest request) {
        videoService.updateVideo(principal.getUserId(), videoId, request);
        return ApiResponse.success("稿件已更新", null);
    }

    @Operation(summary = "删除视频稿件")
    @DeleteMapping("/{videoId}")
    public ApiResponse<Void> delete(@PathVariable Long videoId,
                                    @AuthenticationPrincipal CurrentPrincipal principal) {
        videoService.deleteVideo(principal.getUserId(), videoId);
        return ApiResponse.success("稿件已删除", null);
    }
}
