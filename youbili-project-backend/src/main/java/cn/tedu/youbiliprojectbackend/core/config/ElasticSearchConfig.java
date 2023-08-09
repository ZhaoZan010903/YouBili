package cn.tedu.youbiliprojectbackend.core.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class ElasticSearchConfig {

    @Value("${youbili.els.url}")
    String Url;

    @Value("${youbili.els.port}")
    Integer port;

    @Value("${youbili.els.scheme}")
    String scheme;

    //注入IOC容器
    @Bean
    public ElasticsearchClient elasticsearchClient() {
        log.debug("ELasticsearch配置:URL:{},Port:{},scheme:{}", Url, port, scheme);
        RestClient client = RestClient.builder(new HttpHost(Url, port, scheme)).build();
        ElasticsearchTransport transport = new RestClientTransport(client, new JacksonJsonpMapper());
        return new ElasticsearchClient(transport);
    }

}
