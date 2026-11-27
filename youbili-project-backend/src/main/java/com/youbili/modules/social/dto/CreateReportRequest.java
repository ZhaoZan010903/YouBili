package com.youbili.modules.social.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class CreateReportRequest {

    @NotBlank(message = "请选择举报目标类型")
    @Pattern(regexp = "^(VIDEO|COMMENT|USER)$", message = "目标类型仅支持 VIDEO / COMMENT / USER")
    private String targetType;

    @NotNull(message = "请指定举报对象 ID")
    private Long targetId;

    @NotBlank(message = "请选择违规性质")
    @Size(max = 64, message = "违规性质长度超限")
    private String category;

    private List<String> reasons;

    @NotBlank(message = "请填写违规详细说明")
    @Size(max = 1000, message = "详细说明不能超过 1000 字")
    private String description;
}
