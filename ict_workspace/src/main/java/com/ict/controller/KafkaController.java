package com.ict.controller;

import com.ict.kafka.KafkaSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ：wangsr
 * @description：
 * @date ：Created in 2021/7/7 0007 15:05
 */
@RestController
@RequestMapping("/kafka")
public class KafkaController {
    @Autowired
    private KafkaSender kafkaSender;

    @GetMapping("/kafka/send")
    public void kafkaSender() {
        kafkaSender.sendChannelMess("seckill", "发送kafka消息");
    }
}
