package com.youbili.modules.social.controller;

import com.youbili.common.response.ApiResponse;
import com.youbili.common.response.PageResult;
import com.youbili.common.security.CurrentPrincipal;
import com.youbili.modules.social.dto.CreateCommentRequest;
import com.youbili.modules.social.dto.CreateReplyRequest;
import com.youbili.modules.social.dto.CreateReportRequest;
import com.youbili.modules.social.entity.Notice;
import com.youbili.modules.social.service.CommentService;
import com.youbili.modules.social.service.SocialService;
import com.youbili.modules.social.vo.CommentVO;
import com.youbili.modules.video.vo.VideoCardVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

import java.util.List;
import java.util.Map;

@Tag(name = "社区互动（评论/动态/通知/举报）")
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class SocialController {

    private final CommentService commentService;
    private final SocialService socialService;

    @Operation(summary = "分页获取视频评论与回复树")
    @GetMapping("/videos/{videoId}/comments")
    public ApiResponse<PageResult<CommentVO>> listComments(
            @PathVariable Long videoId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "hot") String sort) {
        return ApiResponse.success(commentService.listComments(videoId, page, size, sort));
    }

    @Operation(summary = "发表主评论")
    @PostMapping("/videos/{videoId}/comments")
    public ApiResponse<Map<String, Long>> addComment(@PathVariable Long videoId,
                                                     @AuthenticationPrincipal CurrentPrincipal principal,
                                                     @Valid @RequestBody CreateCommentRequest request) {
        Long id = commentService.addComment(videoId, principal.getUserId(), request);
        return ApiResponse.success("评论发布成功", Map.of("commentId", id));
    }

    @Operation(summary = "回复评论")
    @PostMapping("/comments/{commentId}/replies")
    public ApiResponse<Map<String, Long>> addReply(@PathVariable Long commentId,
                                                   @AuthenticationPrincipal CurrentPrincipal principal,
                                                   @Valid @RequestBody CreateReplyRequest request) {
        Long id = commentService.addReply(commentId, principal.getUserId(), request);
        return ApiResponse.success("回复成功", Map.of("replyId", id));
    }

    @Operation(summary = "删除自己的评论")
    @DeleteMapping("/comments/{commentId}")
    public ApiResponse<Void> deleteComment(@PathVariable Long commentId,
                                           @AuthenticationPrincipal CurrentPrincipal principal) {
        commentService.deleteComment(commentId, principal.getUserId());
        return ApiResponse.success("评论已删除", null);
    }

    @Operation(summary = "获取关注 UP 主的动态视频流")
    @GetMapping("/dynamics")
    public ApiResponse<PageResult<VideoCardVO>> listDynamics(
            @AuthenticationPrincipal CurrentPrincipal principal,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.success(socialService.listDynamics(principal.getUserId(), page, size));
    }

    @Operation(summary = "获取个人消息或系统公告")
    @GetMapping("/notices")
    public ApiResponse<List<Notice>> listNotices(
            @AuthenticationPrincipal CurrentPrincipal principal,
            @RequestParam(defaultValue = "personal") String type) {
        return ApiResponse.success(socialService.listNotices(principal.getUserId(), type));
    }

    @Operation(summary = "标记个人通知为已读")
    @PutMapping("/notices/{noticeId}/read")
    public ApiResponse<Void> markRead(@PathVariable Long noticeId,
                                      @AuthenticationPrincipal CurrentPrincipal principal) {
        socialService.markNoticeRead(principal.getUserId(), noticeId);
        return ApiResponse.success();
    }

    @Operation(summary = "删除单条个人通知")
    @DeleteMapping("/notices/{noticeId}")
    public ApiResponse<Void> deleteNotice(@PathVariable Long noticeId,
                                          @AuthenticationPrincipal CurrentPrincipal principal) {
        socialService.deletePersonalNotice(principal.getUserId(), noticeId);
        return ApiResponse.success("通知已删除", null);
    }

    @Operation(summary = "提交违规举报")
    @PostMapping("/reports")
    public ApiResponse<Map<String, Long>> submitReport(@AuthenticationPrincipal CurrentPrincipal principal,
                                                       @Valid @RequestBody CreateReportRequest request) {
        Long id = socialService.submitReport(principal.getUserId(), request);
        return ApiResponse.success("举报已提交，感谢您的反馈", Map.of("reportId", id));
    }
}
