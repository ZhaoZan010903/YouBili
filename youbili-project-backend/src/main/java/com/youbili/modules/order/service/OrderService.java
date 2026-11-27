package com.youbili.modules.order.service;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.youbili.common.exception.ServiceException;
import com.youbili.common.response.ServiceCode;
import com.youbili.config.AlipayProperties;
import com.youbili.modules.order.dto.CreateOrderRequest;
import com.youbili.modules.order.entity.Order;
import com.youbili.modules.order.entity.VipPlan;
import com.youbili.modules.order.mapper.OrderMapper;
import com.youbili.modules.order.vo.OrderVO;
import com.youbili.modules.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    public static final String STATUS_PENDING = "待支付";
    public static final String STATUS_PAID = "已支付";

    private static final DateTimeFormatter ORDER_TIME_FMT = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    private final OrderMapper orderMapper;
    private final UserMapper userMapper;
    private final AlipayProperties alipayProperties;
    private final ObjectMapper objectMapper;

    public List<OrderVO.VipPlanVO> listPlans() {
        return Arrays.stream(VipPlan.values())
                .map(p -> new OrderVO.VipPlanVO(
                        p.getCode(), p.getName(), p.getPrice(), p.getDurationDays(), p.getPrivileges()))
                .toList();
    }

    public List<OrderVO> listUserOrders(Long userId) {
        return orderMapper.selectList(
                new LambdaQueryWrapper<Order>()
                        .eq(Order::getUserId, userId)
                        .orderByDesc(Order::getOrderId)
        ).stream().map(this::toVO).toList();
    }

    @Transactional(rollbackFor = Exception.class)
    public OrderVO createOrder(Long userId, CreateOrderRequest request) {
        VipPlan plan = VipPlan.fromCode(request.getPlanCode());
        if (plan == null) {
            throw new ServiceException(ServiceCode.BAD_REQUEST, "无效的会员套餐编码");
        }
        String orderNumber = "YB" + LocalDateTime.now().format(ORDER_TIME_FMT)
                + UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();

        Order order = new Order();
        order.setUserId(userId);
        order.setTradeName(plan.getName());
        order.setOrderNumber(orderNumber);
        order.setPrice(plan.getPrice());
        order.setPayment(request.getPayment().trim());
        order.setStatus(STATUS_PENDING);
        orderMapper.insert(order);
        return toVO(order);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteOrder(Long userId, Long orderId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new ServiceException(ServiceCode.NOT_FOUND, "订单不存在");
        }
        if (!Objects.equals(order.getUserId(), userId)) {
            throw new ServiceException(ServiceCode.FORBIDDEN, "无权操作他人订单");
        }
        if (STATUS_PAID.equals(order.getStatus())) {
            throw new ServiceException(ServiceCode.CONFLICT, "已支付订单不可删除");
        }
        orderMapper.deleteById(orderId);
    }

    public String createAlipayForm(Long userId, String orderNumber) {
        Order order = orderMapper.selectOne(
                new LambdaQueryWrapper<Order>().eq(Order::getOrderNumber, orderNumber)
        );
        if (order == null) {
            throw new ServiceException(ServiceCode.NOT_FOUND, "订单不存在");
        }
        if (!Objects.equals(order.getUserId(), userId)) {
            throw new ServiceException(ServiceCode.FORBIDDEN, "无权支付他人订单");
        }
        if (STATUS_PAID.equals(order.getStatus())) {
            throw new ServiceException(ServiceCode.CONFLICT, "该订单已完成支付");
        }
        if (!StringUtils.hasText(alipayProperties.getAppId())
                || !StringUtils.hasText(alipayProperties.getAppPrivateKey())
                || !StringUtils.hasText(alipayProperties.getAlipayPublicKey())) {
            throw new ServiceException(ServiceCode.PAYMENT_ERROR, "支付宝沙箱参数尚未配置，请先在环境变量中设置 ALIPAY_* 配置");
        }

        try {
            AlipayClient alipayClient = new DefaultAlipayClient(
                    alipayProperties.getGatewayUrl(),
                    alipayProperties.getAppId(),
                    alipayProperties.getAppPrivateKey(),
                    "JSON",
                    "UTF-8",
                    alipayProperties.getAlipayPublicKey(),
                    "RSA2"
            );
            AlipayTradePagePayRequest payRequest = new AlipayTradePagePayRequest();
            payRequest.setNotifyUrl(alipayProperties.getNotifyUrl());
            payRequest.setReturnUrl(alipayProperties.getReturnUrl());
            String bizContent = objectMapper.writeValueAsString(Map.of(
                    "out_trade_no", order.getOrderNumber(),
                    "total_amount", order.getPrice().toPlainString(),
                    "subject", order.getTradeName(),
                    "product_code", "FAST_INSTANT_TRADE_PAY"
            ));
            payRequest.setBizContent(bizContent);
            return alipayClient.pageExecute(payRequest).getBody();
        } catch (AlipayApiException e) {
            log.error("Alipay pageExecute failed for order {}", orderNumber, e);
            throw new ServiceException(ServiceCode.PAYMENT_ERROR, "创建支付宝支付表单失败");
        } catch (Exception e) {
            log.error("Failed to build Alipay request", e);
            throw new ServiceException(ServiceCode.PAYMENT_ERROR, "支付请求构造异常");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public String handleAlipayNotify(Map<String, String> params) {
        if (!"TRADE_SUCCESS".equals(params.get("trade_status"))) {
            return "success";
        }
        try {
            boolean verified = AlipaySignature.rsaCheckV1(
                    params, alipayProperties.getAlipayPublicKey(), "UTF-8", "RSA2");
            if (!verified) {
                log.warn("Alipay callback RSA2 signature verification failed");
                return "failure";
            }

            String outTradeNo = params.get("out_trade_no");
            String tradeNo = params.get("trade_no");
            String totalAmountStr = params.get("total_amount");

            Order order = orderMapper.selectOne(
                    new LambdaQueryWrapper<Order>().eq(Order::getOrderNumber, outTradeNo)
            );
            if (order == null) {
                log.warn("Alipay callback order not found: {}", outTradeNo);
                return "failure";
            }
            // 幂等处理：已支付直接返回 success
            if (STATUS_PAID.equals(order.getStatus())) {
                return "success";
            }
            // 校验实付金额与数据库订单金额一致，防篡改
            if (!StringUtils.hasText(totalAmountStr)
                    || new BigDecimal(totalAmountStr).compareTo(order.getPrice()) != 0) {
                log.error("Alipay amount mismatch for order {}: expected {}, actual {}",
                        outTradeNo, order.getPrice(), totalAmountStr);
                return "failure";
            }

            order.setZfbNumber(tradeNo);
            order.setStatus(STATUS_PAID);
            order.setPayTime(LocalDateTime.now());
            orderMapper.updateById(order);

            // 激活用户大会员状态
            userMapper.updateMembershipStatus(order.getUserId(), "Active");
            log.info("Order {} paid via Alipay ({}), user {} upgraded to Active VIP",
                    outTradeNo, tradeNo, order.getUserId());
            return "success";
        } catch (Exception e) {
            log.error("Error handling Alipay async notify", e);
            return "failure";
        }
    }

    private OrderVO toVO(Order order) {
        return OrderVO.builder()
                .orderId(order.getOrderId())
                .tradeName(order.getTradeName())
                .orderNumber(order.getOrderNumber())
                .zfbNumber(order.getZfbNumber())
                .price(order.getPrice())
                .payment(order.getPayment())
                .status(order.getStatus())
                .paid(STATUS_PAID.equals(order.getStatus()))
                .payTime(order.getPayTime())
                .gmtCreate(order.getGmtCreate())
                .build();
    }
}
