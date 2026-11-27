package com.youbili.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "youbili.jwt")
public class JwtProperties {

    private String secretKey;
    private long expirationMinutes = 10080L;
}
