package com.ict.api.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *  Feign 客户端配置
 *  以下配置需配置在服务调用方
 *logging:
 *   level:
 *     com.ict.api.user.RemoteUsercenterService: DEBUG
 *  @author wsr
 *  @date 2020/02/08
 */
@Configuration
public class FeignConfiguration {
    @Bean
    Logger.Level feignLoggerLevel() {
        // 这里记录所有，根据实际情况选择合适的日志level
        return Logger.Level.FULL;
    }
}
