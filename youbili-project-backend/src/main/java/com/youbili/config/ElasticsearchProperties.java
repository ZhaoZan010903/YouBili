package com.youbili.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "youbili.elasticsearch")
public class ElasticsearchProperties {

    private boolean enabled = false;
    private String host = "localhost";
    private int port = 9200;
    private String scheme = "http";
}
