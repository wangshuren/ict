package com.ict.websocket.controller;

import com.ict.websocket.msg.WarnMsg;
import com.ict.websocket.service.WarnMsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @Description:
 * @Author wangsr
 * @Date 2021/2/23
 * @Version 1.0
 */
@RestController
public class IndexController {
    @Autowired
    WarnMsgService warnMsgService;
    /**
     * 推送消息测试
     */
    @GetMapping("/push")
    public void initMsg(String id) {
        WarnMsg warnMsg = new WarnMsg();
        String format = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        warnMsg.setTitle(format);
        warnMsg.setBody("吃了没？");
        warnMsgService.pushWithTopic(id, warnMsg);
    }
}