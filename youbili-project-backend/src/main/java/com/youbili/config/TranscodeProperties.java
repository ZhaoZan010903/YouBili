package com.youbili.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "youbili.transcode")
public class TranscodeProperties {

    private boolean enabled = false;
    private String ffmpegPath = "ffmpeg";
}
