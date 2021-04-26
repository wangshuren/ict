package com.ict.config;

import lombok.RequiredArgsConstructor;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description:
 * @Author wangsr
 * @Date 2021/4/26
 * @Version 1.0
 */
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Configuration
public class ElasticConfig {
//    @Autowired
//    private ElasticProperties elasticProperties;

    private final ElasticProperties elasticProperties;

    @Bean
    public RestHighLevelClient restHighLevelClient() {
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(
                elasticProperties.getUsername(), elasticProperties.getPassword()
        ));
        // 初始化ES客户端的构造器
        RestClientBuilder builder = RestClient.builder(httpHostHandlerDev());
        // 异步的请求配置
        builder.setRequestConfigCallback(builder1 -> {
            // 连接超时时间 默认-1
            builder1.setConnectTimeout(Integer.parseInt(elasticProperties.getConnectTimeout()));
            //
            builder1.setSocketTimeout(Integer.parseInt(elasticProperties.getSocketTimeout()));
            // 获取连接的超时时间 默认-1
            builder1.setConnectionRequestTimeout(Integer.parseInt(elasticProperties.getConnectionRequestTimeout()));
            return builder1;
        });
        // 异步的httpclient连接数配置
        builder.setHttpClientConfigCallback(httpAsyncClientBuilder -> {
            // 最大连接数
            httpAsyncClientBuilder.setMaxConnTotal(Integer.parseInt(elasticProperties.getMaxConnTotal()));
            // 最大路由连接数
            httpAsyncClientBuilder.setMaxConnPerRoute(Integer.parseInt(elasticProperties.getMaxConnPerRoute()));
            // 赋予连接凭证
            httpAsyncClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
            return httpAsyncClientBuilder;
        });
        return new RestHighLevelClient(builder);
    }

    /**
     * 为了应对集群部署的es，使用以下写法，返回HttpHost数组
     */
    private HttpHost[] httpHostHandlerDev() {
        String[] hosts = elasticProperties.getHttpHost().split(",");
        HttpHost[] httpHosts = new HttpHost[hosts.length];
        for (int i = 0; i < hosts.length; i++) {
            String ip = hosts[i].split(":")[0];
            int port = Integer.parseInt(hosts[i].split(":")[1]);
            httpHosts[i] = new HttpHost(ip, port, "http");
        }
        return httpHosts;
    }
}