package com.ict.rabbitmq.listener;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;

/**
 * @author ：wangsr
 * @description：
 * @date ：Created in 2021/7/7 0007 9:11
 */
public class Exchange {
    //配置队列
    @Bean
    public Queue topicQueue1() {
        return new Queue("topicQueue1");
    }

    @Bean
    public Queue topicQueue2() {
        return new Queue("topicQueue2");
    }
    //配置交换器
    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange("topicExchange");
    }
    //配置绑定
    @Bean
    public Binding topicBinding1(Queue topicQueue1, TopicExchange topicExchange) {
        return BindingBuilder.bind(topicQueue1).to(topicExchange).with("topic.*");
    }

    @Bean
    public Binding topicBinding2(Queue topicQueue2, TopicExchange topicExchange) {
        return BindingBuilder.bind(topicQueue2).to(topicExchange).with("topic.#");
    }
}
