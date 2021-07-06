package com.ict.service;

import cn.hutool.json.JSONObject;
import com.ict.common.Constants;
import com.ict.sender.RabbitmqMessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ：wangsr
 * @description：
 * @date ：Created in 2021/7/6 0006 15:40
 */
@Service
public class RabbitmqService {

    @Autowired
    protected RabbitmqMessageSender rabbitmqMessageSender;

    public void testRa() throws Exception{
        //直接发送给rabbitmq
        Map<String, Object> attrMap = new HashMap<>();
        attrMap.put(Constants.MSG_TOPIC, "topic-ict");
        attrMap.put(Constants.MSG_PARTNERID, "ict");

        JSONObject message = new JSONObject();
        message.putOpt("name", "ming");
        message.putOpt("age", "20");
        message.putOpt("height", "1.8");
        for (int i=0;i<100000;i++) {
            message.putOpt("age", i + "");
            rabbitmqMessageSender.send("upstream-data-touas", message, attrMap);
        }
//        rabbitmqMessageSender.send("upstream-data-touas", message, attrMap);

    }
}
