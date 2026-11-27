package com.youbili.modules.video.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SendBarrageRequest {

    @NotNull(message = "弹幕时间不能为空")
    @DecimalMin(value = "0.0", message = "弹幕时间不能为负数")
    private Float time;

    @NotBlank(message = "弹幕内容不能为空")
    @Size(max = 100, message = "弹幕内容不能超过 100 字")
    private String text;

    @Pattern(regexp = "^#[0-9a-fA-F]{6}$", message = "颜色格式需为十六进制如 #FFFFFF")
    private String color = "#FFFFFF";

    @Pattern(regexp = "^(scroll|top|bottom)$", message = "弹幕模式仅支持 scroll / top / bottom")
    private String type = "scroll";
}
