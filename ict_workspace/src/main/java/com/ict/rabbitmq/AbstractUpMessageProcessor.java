package com.ict.rabbitmq;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ict.common.Constants;
import com.ict.handler.BaseMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 设备上报数据处理逻辑
 * 将消息转发上层应用
 */
public abstract class AbstractUpMessageProcessor extends BaseMessageHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractUpMessageProcessor.class);

    /**
     * 消息处理业务逻辑
     *
     * @param queueName
     * @param messageStr
     * @param attrMap
     * @throws Exception
     */
    @Override
    public void handle(String queueName, String messageStr, Map<String, Object> attrMap) throws Exception {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("AbstractUpMessageProcessor recieve msg from mq,queue:{}，message:{}，attrMap:{}", queueName, messageStr, attrMap);
        }

        long startTime = System.currentTimeMillis();
        String msgId = null;
        try {
            //业务处理

        } catch (Exception e) {
            LOGGER.error("recieve msg from rabbitmq error,queue={},msgId={},message={},attr={},timecost(ms)={}", queueName, msgId, messageStr, JSONUtil.toJsonStr(attrMap), (System.currentTimeMillis() - startTime), e);
        } finally {
            LOGGER.info("recieve msg from rabbitmq,queue={},msgId={},message={},attr={},timecost(ms)={}", queueName, msgId, messageStr, JSONUtil.toJsonStr(attrMap), (System.currentTimeMillis() - startTime));
        }

    }


//    /**
//     * 获取推送类型，如全部、推给指定用户获取除指定用户外的其他用户
//     *
//     * @param messageJson
//     * @return
//     */
//    public abstract int getPushType(JSONObject messageJson);
//
//
//    /**
//     * 获取消息推送qos
//     *
//     * @param messageJson
//     * @return
//     */
//    public abstract int getQos(JSONObject messageJson);
//
//    /**
//     * 获取设备关联的用户列表
//     *
//     * @param pushType
//     * @param messageJson
//     * @return
//     * @throws Exception
//     */
//    public abstract List<PushPartnerBO> getUserList(int pushType, JSONObject messageJson) throws Exception;
//
//    /**
//     * 获取需要推送的三方
//     *
//     * @param uIdList
//     * @return
//     * @throws Exception
//     */
//    public abstract List<PushPartnerBO> getPushPartners(List<String> uIdList) throws Exception;
//
//    /**
//     * 发送消息到上层应用
//     *
//     * @param pushPartner
//     * @param messageJson
//     * @param sourceMqttTopic
//     * @param qos
//     * @param msgId
//     * @throws Exception
//     */
//    public abstract void send(PushPartnerBO pushPartner, JSONObject messageJson, String sourceMqttTopic, int qos, String msgId) throws Exception;
//
//    /**
//     * 业务后处理
//     * 推送kafka
//     *
//     * @param messageJson
//     * @throws Exception
//     */
//    public abstract void postHandle(JSONObject messageJson) throws Exception;
//
//    /**
//     * 校验消息属性
//     *
//     * @param attrMap
//     * @throws BootException
//     */
//    public void checkMqAttr(Map<String, Object> attrMap) throws BootException {
//
//        if (attrMap == null) {
//            throw new BootException(ErrorCodeConstants.MESSAGE_ATTRIBUTE_IS_NULL, "message attribute is null.");
//        }
//
//        String partnerId = (String) attrMap.get(Constants.RABBIT_MSG_ATTR_TPPID);
//
//        // 校验三方id
//        if (isIllegalPartner(partnerId)) {
//            throw new BootException(ErrorCodeConstants.PARTNER_IS_NOT_VALID, "partner is not valid");
//        }
//
//        // 校验topic
//        String sourceMqttTopic = (String) attrMap.get(Constants.RABBIT_MSG_ATTR_TOPIC);
//        if (sourceMqttTopic == null) {
//            throw new BootException(ErrorCodeConstants.TOPIC_IS_NOT_VALID, "source mqtt topic is not valid.");
//        }
//    }
//
//    /**
//     * 校验接收的消息
//     *
//     * @param messageStr
//     * @throws BootException
//     */
//    private void checkMqMsg(String messageStr) throws BootException {
//        if (StringUtils.isEmpty(messageStr)) {
//            throw new BootException(ErrorCodeConstants.MESSAGE_IS_NULL, "message is null.");
//        }
//    }
//
//    /**
//     * 校验第三方是否合法
//     */
//    private boolean isIllegalPartner(String partnerId) {
//        if (StringUtils.isEmpty(partnerId)) {
//            return true;
//        }
//
//        Partner[] values = Partner.values();
//        for (Partner partner : values) {
//            if (partner.getPartnerId().equals(partnerId)) {
//                return false;
//            }
//        }
//
//        return true;
//    }
//
//    /**
//     * 安全狗业务处理
//     *
//     * @param sourceMqttTopic
//     * @param messageJson
//     * @throws Exception
//     */
//    public abstract void safeDogProtocolHanler(String sourceMqttTopic, JSONObject messageJson) throws Exception;
//
//    public abstract void wlinkProtocolHanler(String sourceMqttTopic, JSONObject messageJson) throws Exception;
}
