package com.ict.predicate;

import com.ict.config.MyTimeBetweenConfig;
import com.ict.filter.LimitFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * @Description:
 * @Author wangsr
 * @Date 2021/2/24
 * @Version 1.0
 */
@Component
public class MyBetweenRoutePredicateFactory extends AbstractRoutePredicateFactory<MyTimeBetweenConfig> {
    private static final Logger logger = LoggerFactory.getLogger(MyBetweenRoutePredicateFactory.class);

    public MyBetweenRoutePredicateFactory() {
        super(MyTimeBetweenConfig.class);
    }

    @Override
    public Predicate<ServerWebExchange> apply(MyTimeBetweenConfig config) {
        logger.info("apply");
        LocalTime startTime = config.getStartTime();

        LocalTime endTime = config.getEndTime();

        return new Predicate<ServerWebExchange>(){
            @Override
            public boolean test(ServerWebExchange serverWebExchange) {
                logger.info("test");
                LocalTime now = LocalTime.now();
                // 判断当前时间是否在在配置的时间范围类
                return now.isAfter(startTime) && now.isBefore(endTime);
            }
        };

    }

    public List<String> shortcutFieldOrder() {
        logger.info("shortcutFieldOrder");
        return Arrays.asList("startTime", "endTime");
    }
}
