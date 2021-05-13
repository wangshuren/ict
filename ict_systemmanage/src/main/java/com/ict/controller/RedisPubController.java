package com.ict.controller;

import com.ict.service.SendMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: redis发布消息测试
 * @Author wangsr
 * @Date 2021/2/23
 * @Version 1.0
 */
@RestController
@RequestMapping("/redis")
public class RedisPubController {
    @Autowired
    private SendMessageService sendMessageService;

    @GetMapping("/pub")
    public void test() throws InterruptedException {
        sendMessageService.sendMessage("TOPIC", "这是一个测试");
        Thread.sleep(1000);
        sendMessageService.sendMessage("TOPIC", "this is a test");
    }
}