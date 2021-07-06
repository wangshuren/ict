package com.ict.handler;

import cn.hutool.json.JSONUtil;
import com.ict.common.Constants;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ：wangsr
 * @description：
 * @date ：Created in 2021/7/6 0006 14:51
 */
public abstract class BaseMessageHandler implements ChannelAwareMessageListener {
    private static Logger LOG = LoggerFactory.getLogger(BaseMessageHandler.class);
    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        try {
            String queueName = message.getMessageProperties().getConsumerQueue();
            Map<String, Object> attrMap = new HashMap<>();
            String messageContentStr = new String(message.getBody(), "UTF-8");

            Map<String, Object> headers = message.getMessageProperties().getHeaders();
            if (headers != null) {
                String addAttrStr = (String) headers.getOrDefault(Constants.HEADER_ADD_ATTR, null);
                if (addAttrStr != null) {
                    attrMap = JSONUtil.toBean(addAttrStr, HashMap.class);
                }

                handle(queueName, messageContentStr, attrMap);
            } else {
                handle(queueName, messageContentStr, attrMap);
            }
        } catch (Exception e) {
            LOG.error("process msg from rabbitmq queue fail,message={}", message, e);
        }finally {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }
    }

    public abstract void handle(String queueName, String message, Map<String, Object> attrMap) throws Exception;
}
