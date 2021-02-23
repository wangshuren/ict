package com.ict.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import javax.websocket.Session;
import java.io.IOException;

/**
 * @Description: 创建消息订阅监听者类
 * 这个消息订阅监听者类持有webSocket的客户端会话对象（session），当接收到订阅的消息时，
 * 通过这个会话对象（session）将消息发送到前端，从而实现消息的额主动推送
 *
 * @Author wangsr
 * @Date 2021/2/22
 * @Version 1.0
 */
@Component
public class SubscribeListener implements MessageListener {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    //webSocket客户端会话对象
    private Session session;

    /**
     * 接收发布者消息
     * @param message
     * @param bytes
     */
    @Override
    public void onMessage(Message message, byte[] bytes) {
        String msg = new String(message.getBody());
        logger.info("[{}]主题发布：{}", new String(bytes), msg);
        if (session != null && session.isOpen()) {
            try {
                session.getBasicRemote().sendText(msg);
            } catch (IOException e) {
                logger.error("[redis监听器]发布消息异常：{}", e);
            }
        }
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }
}