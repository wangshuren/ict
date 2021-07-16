package com.ict.rabbitmq.config;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 消息发送确认
 * ConfirmCallback
 * ConfirmCallback是一个回调接口，消息发送到 Broker 后触发回调，确认消息是否到达 Broker 服务器，也就是只确认是否正确到达 Exchange 中
 * 我们需要在生产者的配置中添加下面配置，表示开启发布者确认
 * spring.rabbitmq.publisher-confirms=true
 *
 * 然后在生产者的Java配置类实现该接口
 */

/**
 * @author ：wangsr
 * @description：
 * @date ：Created in 2021/7/8 0008 10:13
 */
@Component
public class RabbitTemplateConfig implements RabbitTemplate.ConfirmCallback {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void initRabbitTemplate() {
        // 设置生产者消息确认
        rabbitTemplate.setConfirmCallback(this);

    }

    @Override
    public void confirm(CorrelationData correlationData, boolean b, String s) {
        System.out.println("ack：[{}]" + b);
        if (b) {
            System.out.println("消息到达rabbitmq服务器");
        } else {
            System.out.println("消息可能未到达rabbitmq服务器");
        }
    }
}
