package com.youbili.modules.order.controller;

import com.youbili.common.response.ApiResponse;
import com.youbili.common.security.CurrentPrincipal;
import com.youbili.modules.order.dto.CreateOrderRequest;
import com.youbili.modules.order.service.OrderService;
import com.youbili.modules.order.vo.OrderVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "会员订单与支付宝支付")
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "获取官方大会员套餐列表")
    @GetMapping("/orders/plans")
    public ApiResponse<List<OrderVO.VipPlanVO>> plans() {
        return ApiResponse.success(orderService.listPlans());
    }

    @Operation(summary = "获取当前用户的订单列表")
    @GetMapping("/orders")
    public ApiResponse<List<OrderVO>> listMyOrders(@AuthenticationPrincipal CurrentPrincipal principal) {
        return ApiResponse.success(orderService.listUserOrders(principal.getUserId()));
    }

    @Operation(summary = "创建会员订单（价格由服务端按套餐编码锁定）")
    @PostMapping("/orders")
    public ApiResponse<OrderVO> createOrder(@AuthenticationPrincipal CurrentPrincipal principal,
                                            @Valid @RequestBody CreateOrderRequest request) {
        return ApiResponse.success("订单创建成功", orderService.createOrder(principal.getUserId(), request));
    }

    @Operation(summary = "删除自己的未支付订单")
    @DeleteMapping("/orders/{orderId}")
    public ApiResponse<Void> deleteOrder(@PathVariable Long orderId,
                                         @AuthenticationPrincipal CurrentPrincipal principal) {
        orderService.deleteOrder(principal.getUserId(), orderId);
        return ApiResponse.success("订单已删除", null);
    }

    @Operation(summary = "发起支付宝网页支付（返回支付跳转表单 HTML）")
    @PostMapping("/alipay/pay/{orderNumber}")
    public ApiResponse<Map<String, String>> payOrder(@PathVariable String orderNumber,
                                                     @AuthenticationPrincipal CurrentPrincipal principal) {
        String formHtml = orderService.createAlipayForm(principal.getUserId(), orderNumber);
        return ApiResponse.success(Map.of("formHtml", formHtml));
    }

    @Operation(summary = "支付宝异步回调通知")
    @PostMapping("/alipay/notify")
    public String alipayNotify(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        request.getParameterMap().forEach((key, values) -> {
            if (values != null && values.length > 0) {
                params.put(key, String.join(",", values));
            }
        });
        return orderService.handleAlipayNotify(params);
    }
}
