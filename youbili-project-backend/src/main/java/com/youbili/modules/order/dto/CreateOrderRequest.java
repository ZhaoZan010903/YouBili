package com.youbili.modules.order.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateOrderRequest {

    @NotBlank(message = "请选择会员套餐")
    private String planCode;

    @NotBlank(message = "请选择支付方式")
    private String payment = "支付宝";
}
