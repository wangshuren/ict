package com.ict.websocket.config;

import com.ict.websocket.constant.RedisKeyConstants;
import com.ict.websocket.listener.RedisTopicListener;
import com.ict.websocket.service.WarnMsgService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.Arrays;

/**
 * @Description:
 * @Author wangsr
 * @Date 2021/2/23
 * @Version 1.0
 */
@Configuration
public class RedisConfig {
    /**
     * 添加spring提供的RedisMessageListenerContainer到容器
     * @param connectionFactory
     * @return
     */
    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        return container;
    }

    /**
     * 添加自己的监听器到容器中（监听指定topic）
     * @param container
     * @param stringRedisTemplate
     * @return
     */
    @Bean
    RedisTopicListener redisTopicListener(RedisMessageListenerContainer container,
            StringRedisTemplate stringRedisTemplate,WarnMsgService warnMsgService) {
        // 指定监听的 topic
        RedisTopicListener redisTopicListener = new RedisTopicListener(container,
                Arrays.asList(new ChannelTopic(RedisKeyConstants.REDIS_TOPIC_MSG)),
                warnMsgService);
        redisTopicListener.setStringRedisSerializer(new StringRedisSerializer());
        redisTopicListener.setStringRedisTemplate(stringRedisTemplate);
        return redisTopicListener;
    }
}