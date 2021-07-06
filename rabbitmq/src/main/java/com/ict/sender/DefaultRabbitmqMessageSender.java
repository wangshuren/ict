package com.ict.sender;

import cn.hutool.json.JSONUtil;
import com.ict.common.Constants;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.MessagePropertiesBuilder;

import java.util.Map;

/**
 * @author ：wangsr
 * @description：
 * @date ：Created in 2021/7/6 0006 14:13
 */
public class DefaultRabbitmqMessageSender implements RabbitmqMessageSender{

    private AmqpTemplate amqpTemplate;

    public DefaultRabbitmqMessageSender(AmqpTemplate amqpTemplate){
        this.amqpTemplate = amqpTemplate;
    }

    @Override
    public void send(String queueName, Object message) throws Exception {
        send(queueName, message, null);
    }

    @Override
    public void send(String queueName, Object message, Map<String, Object> attrMap) throws Exception {
        String messageStr;
        if (message instanceof String) {
            messageStr = (String) message;
        } else {
            messageStr = JSONUtil.toJsonStr(message);
        }
        sendInternal(queueName, messageStr, attrMap);
    }

    private void sendInternal(String queue, String msg, Map<String, Object> attrMap) throws Exception {
        MessageProperties props = MessagePropertiesBuilder
                .newInstance()
                .setContentType(MessageProperties.CONTENT_TYPE_JSON)
                .setHeader(Constants.HEADER_ADD_ATTR, attrMap == null ? null : JSONUtil.toJsonStr(attrMap))
                .build();
        Message msgObj = new Message(msg.getBytes("UTF-8"), props);
        amqpTemplate.send(queue, msgObj);
    }


    @Override
    public void sendToExchange(String exchangeName, Object message) throws Exception {
        sendToExchange(exchangeName, message, null);
    }

    @Override
    public void sendToExchange(String exchangeName, Object message, Map<String, Object> attrMap) throws Exception {

        String messageStr;
        if (message instanceof String) {
            messageStr = (String) message;
        } else {
            messageStr = JSONUtil.toJsonStr(message);
        }
        sendToExchangeInternal(exchangeName, messageStr, attrMap);
    }

    private void sendToExchangeInternal(String exchangeName, String msg, Map<String, Object> attrMap) throws Exception {
        MessageProperties props = MessagePropertiesBuilder
                .newInstance()
                .setContentType(MessageProperties.CONTENT_TYPE_JSON)
                .setHeader(Constants.HEADER_ADD_ATTR, attrMap == null ? null : JSONUtil.toJsonStr(attrMap))
                .build();

        Message msgObj = new Message(msg.getBytes("UTF-8"), props);
        amqpTemplate.send(exchangeName, null, msgObj);
    }
}
