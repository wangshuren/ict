package com.ict.rabbitmq.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 消息确认种类（事务机制、批量确认、异步确认）
 * 事务机制
 *      我们在channel对象中可以看到 txSelect(),txCommit(),txrollback() 这些方法，分别对应着开启事务，提交事务，回滚。由于使用事务会造成生产者与Broker交互次数增加，造成性能资源的浪费，而且事务机制是阻塞的，在发送一条消息后需要等待RabbitMq回应，之后才能发送下一条，因此事务机制不提倡，大家在网上也很少看到RabbitMq使用事务进行消息确认的。
 * 批量确认
 *      批量其实是一个节约资源的操作，但是在RabbitMq中我们使用批量操作会造成消息重复消费，原因是批量操作是使客户端程序定期或者消息达到一定量，来调用方法等待Broker返回，这样其实是一个提高效率的做法，但是如果出现消息重发的情况，当前这批次的消息都需要重发，这就造成了重复消费，因此批量确认的操作性能没有提高反而下降
 * 异步确认（使用）
 *      异步确认虽然编程逻辑比上两个要复杂，但是性价比最高，无论是可靠性还是效率都没得说，他是利用回调函数来达到消息可靠性传递的，笔者接触过RocketMq，这个中间件也是通过函数回调来保证是否投递成功，下面就让我们来详细讲解异步确认是怎么实现的
 *
 *
 */

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

    /**
     *  Headers exchange
     *  不处理路由键。而是根据发送的消息内容中的headers属性进行匹配。在绑定Queue与Exchange时指定一组键值对；当消息发送到RabbitMQ时会取到该消息的headers与Exchange绑定时指定的键值对进行匹配；如果完全匹配则消息会路由到该队列，否则不会路由到该队列。headers属性是一个键值对，可以是Hashtable，键值对的值可以是任何类型。而fanout，direct，topic 的路由键都需要要字符串形式的。
     *
     * 匹配规则x-match有下列两种类型：
     *
     * x-match = all ：表示所有的键值对都匹配才能接受到消息
     *
     * x-match = any ：表示只要有键值对匹配就能接受到消息
     */
}
