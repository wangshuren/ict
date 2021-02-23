package com.ict.websocket.msg;

/**
 * @Description:
 * @Author wangsr
 * @Date 2021/2/23
 * @Version 1.0
 */
public class TopicMsg {
    private String userId;

    private WarnMsg msg;

    public TopicMsg(String userId, WarnMsg msg) {
        this.userId = userId;
        this.msg = msg;
    }

    public TopicMsg() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public WarnMsg getMsg() {
        return msg;
    }

    public void setMsg(WarnMsg msg) {
        this.msg = msg;
    }
}