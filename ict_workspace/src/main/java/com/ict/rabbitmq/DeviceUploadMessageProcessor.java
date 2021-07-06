/**
 * Copyright (C) 2006-2018 Wulian Corp All rights reserved
 */
package com.ict.rabbitmq;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ict.annotation.QueueConsumer;
import com.ict.common.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 接收设备上报的消息队列
 * 不再区分设备列表返回的消息还是控制返回的消息
 * 这一步是设备上报的消息然后DAS透传到这个队列
 */
@Component
@QueueConsumer(queue = Constants.RABBIT_QUEUE_UPSTREAM_MSG_TO_UAS, currentConsumer = 24)
public class DeviceUploadMessageProcessor extends AbstractUpMessageProcessor {

    private static Logger LOG = LoggerFactory.getLogger(DeviceUploadMessageProcessor.class);

    /**
     * 对消息进行一些过滤
     */
    @Override
    public void handle(String queueName, String messageStr, Map<String, Object> attrMap) throws Exception {
        //转对象
        JSONObject msgJson = JSONUtil.parseObj(messageStr);

        super.handle(queueName, messageStr, attrMap);
    }
}
