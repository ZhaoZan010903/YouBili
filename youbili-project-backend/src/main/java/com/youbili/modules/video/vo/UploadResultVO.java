package com.youbili.modules.video.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UploadResultVO {

    private String url;
    private String fileName;
    private Long size;
}
