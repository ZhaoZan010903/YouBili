package com.youbili.modules.social.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateReplyRequest {

    private Long parentReplyId;

    @NotBlank(message = "回复内容不能为空")
    @Size(max = 500, message = "回复内容不能超过 500 字")
    private String replyText;
}
