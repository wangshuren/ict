package com.ict.websocket.listener;

import com.alibaba.fastjson.JSON;
import com.ict.websocket.constant.RedisKeyConstants;
import com.ict.websocket.msg.TopicMsg;
import com.ict.websocket.msg.WarnMsg;
import com.ict.websocket.service.WarnMsgService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.Topic;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @Description: 自定义的topic的监听器
 * @Author wangsr
 * @Date 2021/2/23
 * @Version 1.0
 */
public class RedisTopicListener implements MessageListener {
    private final static Logger log = LoggerFactory.getLogger(RedisTopicListener.class);

    private StringRedisSerializer stringRedisSerializer;
    private StringRedisTemplate stringRedisTemplate;

    private WarnMsgService warnMsgService;

    public WarnMsgService getWarnMsgService() {
        return warnMsgService;
    }

    public void setWarnMsgService(WarnMsgService warnMsgService) {
        this.warnMsgService = warnMsgService;
    }

    public RedisTopicListener(RedisMessageListenerContainer listenerContainer, List<ChannelTopic> topics, WarnMsgService warnMsgService) {
        this(listenerContainer, topics);
        this.warnMsgService = warnMsgService;
    }

    public RedisTopicListener(RedisMessageListenerContainer listenerContainer, List< ? extends Topic> topics) {
        listenerContainer.addMessageListener(this, topics);
    }

//    public RedisTopicListener(RedisMessageListenerContainer container, List<ChannelTopic> topics, WarnMsgService warnMsgService) {
//    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String patternStr = stringRedisSerializer.deserialize(pattern);
        String channel = stringRedisSerializer.deserialize(message.getChannel());
        String body = stringRedisSerializer.deserialize(message.getBody());
        log.info("event = {}, message.channel = {},  message.body = {}", patternStr, channel, body);
        if(RedisKeyConstants.REDIS_TOPIC_MSG.equals(channel)) {
            TopicMsg topicMsg = JSON.parseObject(body, TopicMsg.class);
            String userId = topicMsg.getUserId();
            WarnMsg msg = topicMsg.getMsg();
//			log.debug("receive from topic=[{}] , userId=[{}], msg=[{}]", RedisKeyConstants.REDIS_TOPIC_MSG, userId, msg);
            // 发送消息 id 为空就是群发消息
            if(StringUtils.isEmpty(userId)) {
                warnMsgService.push(msg);
            } else {
                warnMsgService.push(userId, msg);
            }
        }
    }

    public StringRedisSerializer getStringRedisSerializer() {
        return stringRedisSerializer;
    }

    public void setStringRedisSerializer(StringRedisSerializer stringRedisSerializer) {
        this.stringRedisSerializer = stringRedisSerializer;
    }

    public StringRedisTemplate getStringRedisTemplate() {
        return stringRedisTemplate;
    }

    public void setStringRedisTemplate(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }
}