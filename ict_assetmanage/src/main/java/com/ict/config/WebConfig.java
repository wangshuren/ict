package com.ict.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * @Description: 注入拦截器
 * @Author wangsr
 * @Date 2021/4/27
 * @Version 1.0
 */
//@Configuration // 自定义注解
public class WebConfig extends WebMvcConfigurationSupport  {
    @Bean
    public TokenInterceptor getTokenInterceptor() {
        return new TokenInterceptor();
    }

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getTokenInterceptor());
    }
}