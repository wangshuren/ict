package com.ict.web;

import com.ict.listener.SubscribeListener;
import com.ict.utils.SpringUtils;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description: 创建Websocket服务端类
 * @Author wangsr
 * @Date 2021/2/22
 * @Version 1.0
 */

/**
 * @ServerEndpoint 注解是一个类层次的注解，它的功能主要是将目前的类定义成一个websocket服务器端,注解的值将被用于监听用户连接的终端访问URL地址,
 * 客户端可以通过这个URL来连接到WebSocket服务器端使用springboot的唯一区别是要@Component声明下，而使用独立容器是由容器自己管理websocket的，
 * 但在springboot中连容器都是spring管理的。
 *
 * 虽然@Component默认是单例模式的，但springboot还是会为每个websocket连接初始化一个bean，所以可以用一个静态set保存起来。
 *
 * 注意的是在客户端链接关闭的方法onClose中，一定要 删除之前的订阅监听对象，就是下面这行代码：
 * redisMessageListenerContainer.removeMessageListener(subscribeListener);
 *
 * 否则在浏览器刷一下之后，后台会报如下错误:
 * java.lang.IllegalStateException: The WebSocket session [0] has been closed and no method (apart from close())
 * may be called on a closed session
 *
 * 原因就是当链接关闭之后，session对象就没有了，而订阅者对象还是会接收消息，在用session对象发送消息时会报错。
 * 虽然代码中加了判断 if（null != session && session.isOpen()） { 可以避免报错，但是为了防止内存泄漏，应该把没有用的监听者对象从容器中删除。
 */

@Component
@ServerEndpoint("/websocket/server")
public class WebSocketServer {

    /**
     * 因为@ServerEndpoint不支持注入，所以使用SpringUtils获取IOC实例
     */
    private RedisMessageListenerContainer redisMessageListenerContainer = SpringUtils.getBean(RedisMessageListenerContainer.class);

    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static AtomicInteger onlineCount=new AtomicInteger(0);

    //concurrent包的线程安全Set，用来存放每个客户端对应的webSocket对象。若要实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key可以为用户标识
    private static CopyOnWriteArraySet<WebSocketServer> webSocketSet = new CopyOnWriteArraySet<WebSocketServer>();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    private SubscribeListener subscribeListener;

    /**
     * 连接建立成功调用的方法
     * @param session  可选的参数。session 某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    @OnOpen
    public void onOpen(Session session){
        this.session = session;
        //加入set中
        webSocketSet.add(this);
        //在线数加1
        addOnlineCount();
        System.out.println("有新连接加入！当前在线人数为" + getOnlineCount());
        subscribeListener = new SubscribeListener();
        subscribeListener.setSession(session);
        //设置订阅topic
        redisMessageListenerContainer.addMessageListener(subscribeListener, new ChannelTopic("TOPIC"));
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() throws IOException {
        //从set中删除
        webSocketSet.remove(this);

        //在线数减1
        subOnlineCount();
        redisMessageListenerContainer.removeMessageListener(subscribeListener);
        System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
    }



    /**
     * 收到客户端消息后调用的方法
     * @param message 客户端发送过来的消息
     * @param session 可选的参数
     */

    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("来自客户端的消息:" + message);

        //群发消息
        for(WebSocketServer item: webSocketSet){
            try {
                item.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
        }
    }



    /**
     * 发生错误时调用
     * @param session
     * @param error
     */

    @OnError
    public void onError(Session session, Throwable error){
        System.out.println("发生错误");
        error.printStackTrace();
    }



    /**
     * 这个方法与上面几个方法不一样。没有用注解，是根据自己需要添加的方法。
     * @param message
     * @throws IOException
     */

    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }



    public int getOnlineCount() {
        return onlineCount.get();
    }



    public void addOnlineCount() {
        WebSocketServer.onlineCount.getAndIncrement();
    }



    public void subOnlineCount() {
        WebSocketServer.onlineCount.getAndDecrement();
    }
}