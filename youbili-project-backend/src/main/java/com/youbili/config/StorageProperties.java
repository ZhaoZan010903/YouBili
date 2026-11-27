package com.youbili.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "youbili.storage")
public class StorageProperties {

    private String type = "local";
    private String localDir = "./uploads";
    private String publicBaseUrl = "http://localhost:10001/uploads";
    private Oss oss = new Oss();

    @Data
    public static class Oss {
        private String endpoint;
        private String accessKeyId;
        private String accessKeySecret;
        private String videoBucket;
        private String imageBucket;
    }
}
