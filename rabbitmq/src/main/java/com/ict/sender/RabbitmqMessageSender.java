package com.ict.sender;

import java.util.Map;

/**
 * @author ：wangsr
 * @description：
 * @date ：Created in 2021/7/6 0006 14:10
 */
public interface RabbitmqMessageSender {

    /**
     * 发送消息到队列
     * message为object对象,注意不要用原生类型
     */
    void send(String queueName, Object message) throws Exception;

    /**
     * 发送消息到队列
     */
    void send(String queueName, Object message, Map<String, Object> attrMap) throws Exception;

    /**
     * 发送消息到exchange
     * message为object对象,注意不要用原生类型
     */
    void sendToExchange(String exchangeName, Object message) throws Exception;

    /**
     * 发送消息到exchangeName
     */
    void sendToExchange(String exchangeName, Object message, Map<String, Object> attrMap) throws Exception;
}
