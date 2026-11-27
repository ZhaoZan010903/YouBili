package com.youbili.modules.video.controller;

import com.youbili.common.response.ApiResponse;
import com.youbili.common.security.CurrentPrincipal;
import com.youbili.modules.video.dto.SendBarrageRequest;
import com.youbili.modules.video.service.BarrageService;
import com.youbili.modules.video.vo.BarrageVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "弹幕")
@RestController
@RequestMapping("/api/v1/videos/{videoId}/barrages")
@RequiredArgsConstructor
public class BarrageController {

    private final BarrageService barrageService;

    @Operation(summary = "加载视频弹幕列表")
    @GetMapping
    public ApiResponse<List<BarrageVO>> list(@PathVariable Long videoId) {
        return ApiResponse.success(barrageService.listByVideoId(videoId));
    }

    @Operation(summary = "发送视频弹幕")
    @PostMapping
    public ApiResponse<BarrageVO> send(@PathVariable Long videoId,
                                       @AuthenticationPrincipal CurrentPrincipal principal,
                                       @Valid @RequestBody SendBarrageRequest request) {
        return ApiResponse.success("弹幕发送成功", barrageService.send(videoId, principal.getUserId(), request));
    }
}
