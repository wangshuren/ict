package com.ict.rabbitmq.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @author ：wangsr
 * @description：
 * @date ：Created in 2021/7/6 0006 15:40
 */
@Service
public class RabbitmqService {

    @Autowired
    RabbitTemplate rabbitTemplate;


    /**
     * 简单生产模式 一个消费者（接受全部消息）
     * 当生产端发送消息到交换机，交换机根据消息属性发送到队列，消费者监听绑定队列实现消息的接收和消费逻辑编写
     * 简单模式下，强调的一个队列queue只被一个消费者监听
     * @throws Exception
     */
    public void simpleProduct() throws Exception{
        for (int num = 0; num < 20; num++) {
            rabbitTemplate.convertAndSend("simpleQueue", "简单模式"+num);
        }
    }

    /**
     * 工作模式 多个消费者（竞争接收）
     * 生产者:发送消息到交换机
     *
     * 交换机:根据消息属性将消息发送给队列
     *
     * 消费者:多个消费者,同时绑定监听一个队列,之间形成了争抢消息的效果
     */
    public void workProduct(){
        for (int num = 0; num < 20; num++) {
            rabbitTemplate.convertAndSend("workQueue", "工作模式"+num);
        }
    }

    /**
     * 发布订阅模式 （订阅者全部可以接收）
     * 不计算路由的一种特殊交换机
     *
     */
    public void FanoutProduct(){
        for (int num = 0; num < 10; num++) {
            rabbitTemplate.convertAndSend("fanoutExchange","","发布订阅模式"+num);
        }
    }


    /**
     * 路由模式 routingKey one  （竞争接收）
     *
     * 从路由模式开始，关心的就是消息如何到达的队列，路由模式需要使用的交换机类型就是路由交换机（direct）
     *
     * 生产端：发送消息，在消息中处理消息内容，携带一个routingkey
     *
     * 交换机：接收消息，根据消息的routingkey去计算匹配后端队列的routingkey
     *
     * 队列：存储交换机发送的消息
     *
     * 消费端：简单模式 工作争抢
     *
     */
    public void directProduct1() {
        for (int num = 0; num < 5; num++) {
            rabbitTemplate.convertAndSend("directExchange","one", "发送到路由队列1消息"+num);
        }
    }

    /**
     * 路由模式 routingKey two （竞争接收）
     */
    public void directProduct2() {
        for (int num = 0; num < 5; num++) {
            rabbitTemplate.convertAndSend("directExchange","two", "发送到路由队列2消息"+num);
        }
    }

    /**
     * 主题模式 (竞争接收)
     *
     * 生产端：携带路由key，发送消息到交换机
     *
     * 队列：绑定交换机 和路由不一样，不是一个具体的路由key，而可以使用*和#代替一个范围
     *    * 字符串，只能表示一级
     *    # 多级字符串
     * 交换机：根据匹配规则，将路由key对应发送到队列
     * 消息路由key: 北京市.朝阳区.酒仙桥
     */
    public void topicProduct() {
        for (int num = 0; num < 5; num++) {
            rabbitTemplate.convertAndSend("topicExchange","topic.one", "routkey为topic.one的消息");
        }
        for (int num = 0; num < 5; num++) {
            rabbitTemplate.convertAndSend("topicExchange","topic.one.two", "routkey为topic.one.two的消息");
        }

    }
}
