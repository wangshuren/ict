package com.ict.websocket.service;

import com.ict.websocket.msg.WarnMsg;

/**
 * @Description:
 * @Author wangsr
 * @Date 2021/2/23
 * @Version 1.0
 */
public interface WarnMsgService {
    /**
     * 推送消息
     * @param msg
     */
    void push(WarnMsg msg);

    /**
     * 推送消息
     * @param userId 用户id
     * @param msg
     */
    void push(String userId, WarnMsg msg);

    /**
     * 通过 redis topic 发送消息（群发）
     * @param msg
     */
    void pushWithTopic(WarnMsg msg);

    /**
     * 通过 redis topic 发送消息
     * @param userId
     * @param msg
     */
    void pushWithTopic(String userId, WarnMsg msg);
}