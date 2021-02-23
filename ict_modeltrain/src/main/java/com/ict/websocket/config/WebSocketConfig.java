package com.ict.websocket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;



/**
 * @Description: websocket配置类
 * @Author wangsr
 * @Date 2021/2/23
 * @Version 1.0
 */
@Configuration
public class WebSocketConfig {
    @Bean
    public ServerEndpointExporter serverEndpointConfig() {
        return new ServerEndpointExporter();
    }

//    @Bean
//    public EndpointConfig newConfig() {
//        return new EndpointConfig();
//    }
}