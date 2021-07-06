package com.ict.rabbitmq.controller;

import com.ict.rabbitmq.service.RabbitmqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ：wangsr
 * @description：
 * @date ：Created in 2021/7/6 0006 15:39
 */
@RestController
@RequestMapping("/rabbitmq")
public class RabbitController {

    @Autowired
    private RabbitmqService rabbitmqService;

    @GetMapping("/sendMessage")
    public void testRa() throws Exception {
        rabbitmqService.testRa();
    }

}
