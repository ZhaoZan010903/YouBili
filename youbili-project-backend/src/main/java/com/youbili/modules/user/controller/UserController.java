package com.youbili.modules.user.controller;

import com.youbili.common.response.ApiResponse;
import com.youbili.common.security.CurrentPrincipal;
import com.youbili.modules.user.dto.UpdateProfileRequest;
import com.youbili.modules.user.service.FollowService;
import com.youbili.modules.user.service.UserService;
import com.youbili.modules.user.vo.FollowUserVO;
import com.youbili.modules.user.vo.UserProfileVO;
import com.youbili.modules.user.vo.UserSpaceVO;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "用户空间与关注")
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final FollowService followService;

    @Operation(summary = "获取用户个人空间信息")
    @GetMapping("/{userId}/space")
    public ApiResponse<UserSpaceVO> getSpace(@PathVariable Long userId,
                                             @AuthenticationPrincipal CurrentPrincipal principal) {
        Long currentUserId = principal == null ? null : principal.getUserId();
        return ApiResponse.success(userService.getUserSpace(userId, currentUserId));
    }

    @Operation(summary = "更新当前用户个人资料")
    @PutMapping("/me")
    public ApiResponse<UserProfileVO> updateProfile(@AuthenticationPrincipal CurrentPrincipal principal,
                                                    @Valid @RequestBody UpdateProfileRequest request) {
        return ApiResponse.success("资料已更新", userService.updateProfile(principal.getUserId(), request));
    }

    @Operation(summary = "获取当前用户的关注列表")
    @GetMapping("/me/follows")
    public ApiResponse<List<FollowUserVO>> myFollows(@AuthenticationPrincipal CurrentPrincipal principal) {
        return ApiResponse.success(followService.listFollows(principal.getUserId()));
    }

    @Operation(summary = "获取当前用户的粉丝列表")
    @GetMapping("/me/fans")
    public ApiResponse<List<FollowUserVO>> myFans(@AuthenticationPrincipal CurrentPrincipal principal) {
        return ApiResponse.success(followService.listFans(principal.getUserId()));
    }

    @Operation(summary = "关注指定用户")
    @PostMapping("/{targetUserId}/follow")
    public ApiResponse<Void> follow(@PathVariable Long targetUserId,
                                    @AuthenticationPrincipal CurrentPrincipal principal) {
        followService.follow(principal.getUserId(), targetUserId);
        return ApiResponse.success("关注成功", null);
    }

    @Operation(summary = "取消关注指定用户")
    @DeleteMapping("/{targetUserId}/follow")
    public ApiResponse<Void> unfollow(@PathVariable Long targetUserId,
                                      @AuthenticationPrincipal CurrentPrincipal principal) {
        followService.unfollow(principal.getUserId(), targetUserId);
        return ApiResponse.success("已取消关注", null);
    }

    @Operation(summary = "移除粉丝")
    @DeleteMapping("/me/fans/{followerId}")
    public ApiResponse<Void> removeFan(@PathVariable Long followerId,
                                       @AuthenticationPrincipal CurrentPrincipal principal) {
        followService.removeFan(principal.getUserId(), followerId);
        return ApiResponse.success("已移除该粉丝", null);
    }
}
