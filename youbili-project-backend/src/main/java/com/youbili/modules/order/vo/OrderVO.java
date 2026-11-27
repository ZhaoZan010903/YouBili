package com.youbili.modules.order.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderVO {

    private Long orderId;
    private String tradeName;
    private String orderNumber;
    private String zfbNumber;
    private BigDecimal price;
    private String payment;
    private String status;
    private Boolean paid;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime payTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtCreate;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class VipPlanVO {
        private String code;
        private String name;
        private BigDecimal price;
        private int durationDays;
        private List<String> privileges;
    }
}
