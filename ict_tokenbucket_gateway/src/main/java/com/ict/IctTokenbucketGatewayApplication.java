package com.ict;

import com.ict.resolver.HostAddrKeyResolver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class IctTokenbucketGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(IctTokenbucketGatewayApplication.class, args);
    }

    @Bean
    public HostAddrKeyResolver hostAddrKeyResolver() {
        return new HostAddrKeyResolver();
    }

}
