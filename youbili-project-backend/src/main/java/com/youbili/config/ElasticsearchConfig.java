package com.youbili.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
        ObjectMapper esObjectMapper = new ObjectMapper();
        esObjectMapper.registerModule(new JavaTimeModule());
        esObjectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        esObjectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        ElasticsearchTransport transport = new RestClientTransport(
                restClient,
                new JacksonJsonpMapper(esObjectMapper)
        );
        return new ElasticsearchClient(transport);
    }
}
