package org.dromara.api.aliyun.isi.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

/**
 * RestClient配置工厂类
 *
 * @author Lion Li
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class RestClientConfig {

    private final AliyunIsiProperties aliyunIsiProperties;

    /**
     * 配置阿里云录音文件识别RestClient
     */
    @Bean("aliyunIsiRestClient")
    public RestClient aliyunIsiRestClient() {
        return RestClient.builder()
                .requestFactory(createRequestFactory())
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("Accept", "application/json")
                .build();
    }

    /**
     * 创建支持重试的RestClient
     */
    @Bean("aliyunIsiRestClientWithRetry")
    public RestClient aliyunIsiRestClientWithRetry() {
        return RestClient.builder()
                .requestFactory(createRequestFactory())
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("Accept", "application/json")
                .requestInterceptor(new RetryRequestInterceptor(aliyunIsiProperties))
                .build();
    }

    /**
     * 创建HTTP请求工厂
     */
    private ClientHttpRequestFactory createRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(aliyunIsiProperties.getConnectTimeout());
        factory.setReadTimeout(aliyunIsiProperties.getReadTimeout());
        return factory;
    }

    /**
     * 重试请求拦截器
     */
    @RequiredArgsConstructor
    public static class RetryRequestInterceptor implements RestClientCustomizer {

        private final AliyunIsiProperties properties;

        @Override
        public void customize(RestClient.Builder builder) {
            builder.requestInterceptor((request, body) -> {
                log.debug("发送请求到: {}", request.getURI());
                return request;
            });
        }
    }
} 