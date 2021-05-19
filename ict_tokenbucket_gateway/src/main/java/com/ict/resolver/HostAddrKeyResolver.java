package com.ict.resolver;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @Description: KeyResolver需要实现resolve方法，比如根据Hostname进行限流，则需要用hostAddress去判断。
 * 实现完KeyResolver之后，需要将这个类的Bean注册到Ioc容器中
 * @Author wangsr
 * @Date 2021/2/27
 * @Version 1.0
 */
//@Component
public class HostAddrKeyResolver implements KeyResolver {
    @Override
    public Mono<String> resolve(ServerWebExchange exchange) {
        return Mono.just(exchange.getRequest().getRemoteAddress().getAddress().getHostAddress());
    }

//    @Bean
//    public HostAddrKeyResolver hostAddrKeyResolver() {
//        return new HostAddrKeyResolver();
//    }
}
