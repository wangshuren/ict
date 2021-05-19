package com.ict.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author ：wangsr
 * @description：
 * @date ：Created in 2021/5/19 0019 14:06
 */
@RefreshScope
@Configuration
@Data
public class IctConfig {
    @Value("${ict.token-out-time}")
    private int tokenOuttime;

    @Value("${ict.white-urls}")
    private List<String> whiteUrls;
}
