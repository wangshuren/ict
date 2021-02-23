package com.ict.websocket.service.impl;

import com.alibaba.fastjson.JSON;
import com.ict.websocket.constant.RedisKeyConstants;
import com.ict.websocket.msg.TopicMsg;
import com.ict.websocket.msg.WarnMsg;
import com.ict.websocket.server.WebSocketServer;
import com.ict.websocket.service.WarnMsgService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.WebSocketHandler;

import java.util.Collections;

/**
 * @Description:
 * @Author wangsr
 * @Date 2021/2/23
 * @Version 1.0
 */
@Service
public class WarnMsgServiceImpl implements WarnMsgService {
    private final static Logger log = LoggerFactory.getLogger(WarnMsgServiceImpl.class);

    @Autowired
    WebSocketServer webSocketHandler;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

//    @Override
//    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
//        webSocketHandler = (WebSocketServer)applicationContext.getBean("webSocketHandler", WebSocketHandler.class);
//        Assert.notNull(webSocketHandler, "初始化webSocketHandler成功！");
//    }

    @Override
    public void push(WarnMsg msg) {
//        RyGzry user = CommonUtils.getUser();
//        push(String.valueOf(user.getId()), msg);
        push("", msg);
    }

    @Override
    public void push(String userId, WarnMsg msg) {
        Assert.notNull(msg, "消息对象不能为空！");
        if(msg.getBody() == null) {
            msg.setBody(Collections.emptyMap().toString());
        }
        if(StringUtils.isEmpty(userId)) {
            webSocketHandler.broadcast(JSON.toJSONString(msg));
        } else {
            webSocketHandler.send(userId, JSON.toJSONString(msg));
        }
    }

    /*
     *  向 redis 的 topic 发消息
     * 测试指定的topic的监听器(命令行)
     * 发布订阅
     * SUBSCRIBE redisChat // 订阅主题
     * PSUBSCRIBE it* big* //订阅给定模式的主题
     *
     * PUBLISH redisChat "Redis is a great caching technique" // 发布消息主题
     *
     * PUNSUBSCRIBE it* big* // 取消订阅通配符的频道
     * UNSUBSCRIBE channel it_info big_data // 取消订阅具体的频道
     */
    @Override
    public void pushWithTopic(String userId, WarnMsg msg) {
        if(null == userId) {
            userId = "";
        }
        if(msg == null) {
            log.debug("send to userId = [{}] msg is empty, just ignore!", userId);
            return;
        }
        String body = JSON.toJSONString(new TopicMsg(userId, msg));
        log.debug("send topic=[], msg=[]", RedisKeyConstants.REDIS_TOPIC_MSG, body);
        stringRedisTemplate.convertAndSend(RedisKeyConstants.REDIS_TOPIC_MSG, body);
    }

    @Override
    public void pushWithTopic(WarnMsg msg) {
        pushWithTopic("", msg);
    }
}