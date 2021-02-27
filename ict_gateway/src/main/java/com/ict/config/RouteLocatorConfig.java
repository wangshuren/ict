package com.ict.config;

import com.ict.filter.LimitFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * @Description: 添加自定义路由，添加配置类，RouteLocator构造器中添加filter以及相应的地址信息，设置同一ip同时只能访问一次，多余的将被忽略。另外，由于我们在程序中配置了路由，需要将application.yml中的gateway相关属性删除
 * @Author wangsr
 * @Date 2021/2/26
 * @Version 1.0
 */
@Configuration
public class RouteLocatorConfig {
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        System.out.println("============================RouteLocatorConfig "+ builder.routes());
        return builder
                .routes()
                .route(r -> r.path("/usercenter/**")
                        .filters(f -> f.filter(new LimitFilter(1,
                                1, Duration.ofSeconds(1))))
                        .uri("lb://usercenter")
                        .order(0)
                        .id("usercenter"))
                .build();
    }
}