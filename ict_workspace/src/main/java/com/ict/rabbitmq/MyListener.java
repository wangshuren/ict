package com.ict.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author ：wangsr
 * @description：
 * @date ：Created in 2021/7/7 0007 8:51
 */
@Component
public class MyListener {
//    @RabbitListener(queues = "workQueue")
//    public void workListener2(String message) {
//        System.out.println("工作模式监听器2：" + message);
//    }

//    @RabbitListener(queues = "simpleQueue")
//    public void simpleListener(String message){
//        System.out.println("简单模式监听器2："+message);
//    }
}
