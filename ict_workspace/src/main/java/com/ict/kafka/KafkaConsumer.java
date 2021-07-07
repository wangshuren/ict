package com.ict.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @author ：wangsr
 * @description：消费者
 * @date ：Created in 2021/7/7 0007 14:35
 */
@Component
public class KafkaConsumer {

    /**
     * 监听seckill主题，有消息就读取
     * @param message
     */
    @KafkaListener(topics = {"seckill"})
    public void receiveMessage(String message) {
        // 收到通道的消息之后执行操作
        System.out.println("收到消息: " + message + "，执行相应操作");
    }
}
