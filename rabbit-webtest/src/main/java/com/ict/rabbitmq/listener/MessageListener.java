package com.ict.rabbitmq.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author ：wangsr
 * @description： 消费者
 * @date ：Created in 2021/7/6 0006 19:52
 */
@Component
public class MessageListener {

    @RabbitListener(queues = "simpleQueue")
    public void simpleListener(String message){
        System.out.println("简单模式监听器："+message);
    }

    @RabbitListener(queues = "workQueue")
    public void workListener1(String message) {
        System.out.println("工作模式监听器1：" + message);
    }

    @RabbitListener(queues = "workQueue")
    public void workListener2(String message) {
        System.out.println("工作模式监听器2：" + message);
    }

    @RabbitListener(queues = "fanoutQueue1")
    public void fanoutListener1(String message) {
        System.out.println("发布订阅监听器1：" + message);
    }

    @RabbitListener(queues = "fanoutQueue2")
    public void fanoutListener2(String message) {
        System.out.println("发布订阅监听器2：" + message);
    }

    @RabbitListener(queues = "directQueue1")
    public void directListener1(String message) {
        System.out.println("directQueue1路由模式监听器1：" + message);
    }

    @RabbitListener(queues = "directQueue1")
    public void directListener1p(String message) {
        System.out.println("directQueue1路由模式监听器2：" + message);
    }

    @RabbitListener(queues = "directQueue2")
    public void directListener2(String message) {
        System.out.println("路由模式监听器2：" + message);
    }

    @RabbitListener(queues = "topicQueue1")
    public void topicListener1(String message) {
        System.out.println("topicQueue1通配符监听器1：" + message);
    }
    @RabbitListener(queues = "topicQueue1")
    public void topicListener1p(String message) {
        System.out.println("topicQueue1通配符监听器2：" + message);
    }

    @RabbitListener(queues = "topicQueue2")
    public void topicListener2(String message) {
        System.out.println("通配符监听器2：" + message);
    }
}
