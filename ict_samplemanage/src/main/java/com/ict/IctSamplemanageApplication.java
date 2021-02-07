package com.ict;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * 此处的包扫描问题：
 * 服务调用方的启动类应建在feign服务的相应服务类的上一级才能扫描到服务
 */
@SpringBootApplication
@EnableFeignClients//("com.ict.api.user")
@EnableDiscoveryClient
//这个注解非常重要
//@ComponentScan({"com.ict.api.user.hystrix"})
@EnableHystrix
public class IctSamplemanageApplication {

    public static void main(String[] args) {
        SpringApplication.run(IctSamplemanageApplication.class, args);
    }

}
