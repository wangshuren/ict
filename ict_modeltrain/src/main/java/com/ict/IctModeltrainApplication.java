package com.ict;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class IctModeltrainApplication {

    public static void main(String[] args) {
        SpringApplication.run(IctModeltrainApplication.class, args);
    }

}
