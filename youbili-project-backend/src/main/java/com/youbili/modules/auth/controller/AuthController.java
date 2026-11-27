package com.youbili.modules.auth.controller;

import com.youbili.common.response.ApiResponse;
import com.youbili.common.security.CurrentPrincipal;
import com.youbili.common.security.JwtAuthenticationFilter;
import com.youbili.modules.auth.dto.EmailCodeRequest;
import com.youbili.modules.auth.dto.LoginRequest;
import com.youbili.modules.auth.dto.RegisterRequest;
import com.youbili.modules.auth.service.AuthService;
import com.youbili.modules.auth.vo.LoginResponseVO;
import com.youbili.modules.user.vo.UserProfileVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "认证与账号")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public ApiResponse<LoginResponseVO> login(@Valid @RequestBody LoginRequest request) {
        return ApiResponse.success("登录成功", authService.login(request));
    }

    @Operation(summary = "发送注册邮箱验证码")
    @PostMapping("/email-code")
    public ApiResponse<Void> sendEmailCode(@Valid @RequestBody EmailCodeRequest request) {
        authService.sendVerificationEmail(request.getEmail());
        return ApiResponse.success("验证码已发送至邮箱", null);
    }

    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public ApiResponse<Void> register(@Valid @RequestBody RegisterRequest request) {
        authService.register(request);
        return ApiResponse.success("注册成功", null);
    }

    @Operation(summary = "退出登录并吊销当前令牌")
    @PostMapping("/logout")
    public ApiResponse<Void> logout(HttpServletRequest request) {
        authService.logout(JwtAuthenticationFilter.extractToken(request));
        return ApiResponse.success("已退出登录", null);
    }

    @Operation(summary = "获取当前登录用户信息")
    @GetMapping("/me")
    public ApiResponse<UserProfileVO> me(@AuthenticationPrincipal CurrentPrincipal principal) {
        return ApiResponse.success(authService.getProfile(principal.getUserId()));
    }
}
