package com.ict.service;

import com.ict.utils.SpringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @Description: redis发布消息
 * @Author wangsr
 * @Date 2021/2/23
 * @Version 1.0
 */
@Service
public class SendMessageService {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * redis发布消息，就是往指定频道发消息
     *
     * @param channel 订阅的频道
     * @param message 发布 的内容
     */
    public void sendMessage(String channel, String message) {

        redisTemplate.convertAndSend(channel, message);
        System.out.println("您已经向频道：" + channel + "发布了一条消息，内容是： " + message);
    }
}