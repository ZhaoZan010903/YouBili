package com.youbili.modules.interaction.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RecordHistoryRequest {

    @NotNull(message = "视频ID不能为空")
    private Long videoId;

    @Min(value = 0, message = "观看时长不能为负数")
    private Integer watchDurationSeconds = 0;
}
