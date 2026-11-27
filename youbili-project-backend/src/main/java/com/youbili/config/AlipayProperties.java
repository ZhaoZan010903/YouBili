package com.youbili.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "youbili.alipay")
public class AlipayProperties {

    private String gatewayUrl = "https://openapi-sandbox.dl.alipaydev.com/gateway.do";
    private String appId;
    private String appPrivateKey;
    private String alipayPublicKey;
    private String notifyUrl;
    private String returnUrl;
}
