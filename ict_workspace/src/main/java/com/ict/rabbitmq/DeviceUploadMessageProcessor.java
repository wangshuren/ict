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
import org.springframework.util.StringUtils;

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

        //是否是修改密码操作,如果没有实际修改密码,则忽略不发送
//        if (is509(msgJson) && !changePasswordTrue(msgJson)) {
//            LOG.info("509 but not update password true,not push,ignore this msg={}", messageStr);
//            return;
//        }
        super.handle(queueName, messageStr, attrMap);
    }
//
//    /**
//     * 判断509命令是否实际修改了密码
//     */
//    private boolean changePasswordTrue(JSONObject msgJson) throws Exception {
//        String deviceId = msgJson.getString(Constants.KEY_GW_ID);
//        String password = msgJson.getString(Constants.KEY_GW_PWD);
//        DeviceInfoData device = dcAPI.getDeviceInfo(deviceId);
//
//        // 只有在密码相同的情况下需要查询缓存中修改网关密码标识
//        if (null != password && password.equals(device.getPassword())) {
//            return isChangePassword(deviceId);
//        }
//        return true;
//    }
//
//    /**
//     * 是否是509命令
//     */
//    private boolean is509(JSONObject msgJson) {
//        String cmd = msgJson.getString(Constants.KEY_CMD);
//        if (Cmd.CMD509.getCode().equals(cmd)) {
//            return true;
//        }
//        return false;
//    }
//
//    private boolean isChangePassword(String deviceId) {
//        String redisKey = KEY_DC_FLAG_CHANGE_DEVICE_PASSWORD + deviceId;
//        String data = redisService.get(redisKey);
//
//        // 如果缓存中没有修改网关密码标识，则返回false，标识没有修改网关密码
//        // 缓存只用一次
//        if (StringUtils.isEmpty(data)) {
//            return false;
//        } else {
//            redisService.delete(redisKey);
//            return true;
//        }
//    }
}
