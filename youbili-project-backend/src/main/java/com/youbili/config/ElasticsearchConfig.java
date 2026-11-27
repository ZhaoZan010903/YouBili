package com.youbili.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "youbili.elasticsearch", name = "enabled", havingValue = "true")
public class ElasticsearchConfig {

    private final ElasticsearchProperties properties;

    @Bean(destroyMethod = "close")
    public RestClient elasticsearchRestClient() {
        log.info("Initializing Elasticsearch RestClient: {}://{}:{}",
                properties.getScheme(), properties.getHost(), properties.getPort());
        return RestClient.builder(new HttpHost(properties.getHost(), properties.getPort(), properties.getScheme()))
                .build();
    }

    @Bean
    public ElasticsearchClient elasticsearchClient(RestClient restClient) {
        ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
        return new ElasticsearchClient(transport);
    }
}
