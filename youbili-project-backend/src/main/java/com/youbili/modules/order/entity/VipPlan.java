package com.youbili.modules.order.entity;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Getter
public enum VipPlan {

    VIP_MONTH("VIP_MONTH", "YouBili 月度大会员", new BigDecimal("25.00"), 30,
            List.of("1080P 高码率原画观看", "专属彩色弹幕特权", "免广告流畅播放", "会员专属头像挂件")),
    VIP_QUARTER("VIP_QUARTER", "YouBili 季度大会员", new BigDecimal("68.00"), 90,
            List.of("1080P 高码率原画观看", "专属彩色弹幕特权", "免广告流畅播放", "院线大片与热播番剧畅看")),
    VIP_YEAR("VIP_YEAR", "YouBili 年度大会员", new BigDecimal("168.00"), 365,
            List.of("4K/1080P 全站画质解锁", "尊享炫彩弹幕与专属标识", "全站影视番剧无限畅看", "年度专属客服与优先审核"));

    private final String code;
    private final String name;
    private final BigDecimal price;
    private final int durationDays;
    private final List<String> privileges;

    VipPlan(String code, String name, BigDecimal price, int durationDays, List<String> privileges) {
        this.code = code;
        this.name = name;
        this.price = price;
        this.durationDays = durationDays;
        this.privileges = privileges;
    }

    public static VipPlan fromCode(String code) {
        return Arrays.stream(values())
                .filter(p -> p.code.equalsIgnoreCase(code))
                .findFirst()
                .orElse(null);
    }
}
