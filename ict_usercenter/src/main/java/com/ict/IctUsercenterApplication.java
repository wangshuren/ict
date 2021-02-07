package com.ict;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class IctUsercenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(IctUsercenterApplication.class, args);
    }

}
