package com.ict.rabbitmq;

import cn.hutool.json.JSONObject;
import com.ict.common.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 设备上报数据处理逻辑
 *   wulian协议的业务处理逻辑
 *   icam协议的业务处理逻辑
 *   safedog协议的业务处理逻辑
 */
public class BaseDeviceUploadMessageProcessor extends AbstractUpMessageProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseDeviceUploadMessageProcessor.class);

    /** mqtt转发全部用户 */
    private static final int MQTT_PUSH_ALL = 1;

    /** mqtt转发特定用户 */
    private static final int MQTT_PUSH_UID_APPOINTED = 2;

    /** mqtt转发除特定用户外的所有用户 */
    private static final int MQTT_PUSH_UID_EXCLUSIVE = 3;

    /** mqtt转发特定用户(不用查询用户是否和设备已绑定) */
    private static final int MQTT_PUSH_UID_DIRECT = 4;

    /** 缓存key 用户ID对应的第三方ID */
    private static final String REDIS_KEY_PID_BYUID = "uas:pId:byuid:";

    /** 缓存key 第三方ID对应的第三方信息 */
    private static final String REDIS_KEY_PARTNER_INFO_BYID = "uas:partnerInfo:byid:";

    /** 缓存key 用户对应的授权第三方信息 */
    private static final String REDIS_KEY_OAUTH_UID_PARTNER = "oauth:uId:partner:";

    /** 缓存key 操作V5升V6设备的用户ID */
    private static final String REDIS_KEY_V5_TO_V6_DEV_ID = "uas:v5tov6:devId:";

    /**
     * 当前的区域
     */
    @Value("${env.region}")
    private String currentRegion;

    @Value("${account.sharePartners}")
    private String sharePartners;

    @Value("${catalina.home}/conf/app-conf/templates/")
    private String templateFileUrl;

    private ExecutorService executorService = Executors.newFixedThreadPool(40);

//    /**
//     * 获取qos
//     */
//    @Override
//    public int getQos(JSONObject messageJson) {
//        String cmd = messageJson.getString(Constants.KEY_CMD);
//
//        if (Constants.CMD_509_CHANGE_PASSWORD.equals(cmd)) {
//            return MqttQos.QOS_1.getQos();
//        }
//
//        return MqttQos.QOS_0.getQos();
//    }
//
//    /**
//     * 获取推送类型
//     */
//    @Override
//    public int getPushType(JSONObject messageJson) {
//
//        String cmd = messageJson.getString(Constants.KEY_CMD);
//
//        if (Cmd.CMD01.getCode().equals(cmd)) {
//            String key = REDIS_KEY_V5_TO_V6_DEV_ID + messageJson.getString(Constants.KEY_GW_ID);
//
//            String uId = redisService.get(key);
//
//            if (!StringUtils.isEmpty(uId)) {
//                messageJson.put(Constants.KEY_USER_ID, uId);
//                redisService.delete(key);
//
//                return MQTT_PUSH_UID_DIRECT;
//            }
//        }
//
//        String uIdMqtt = messageJson.getString(Constants.KEY_USER_ID);
//
//        if(StringUtils.isEmpty(uIdMqtt)){
//            return MQTT_PUSH_ALL;
//        }
//
//        Integer mode = messageJson.getInteger(Constants.KEY_MODE);
//
//        if (Constants.CMD_500.equals(cmd) && mode != null && mode != Cmd500Body.MODE_DEVICE_FIRST_ONLINE) {
//            return MQTT_PUSH_UID_APPOINTED;
//        }
//
//        return CmdMqttPushType.getPushTypeByCmd(cmd);
//    }
//
//    /**
//     * 获取用户列表
//     */
//    @Override
//    public List<PushPartnerBO> getUserList(int pushType, JSONObject messageJson) throws Exception{
//        String devID = messageJson.getString("devID");
//        String uIdMqtt = messageJson.getString("userID");
//        String cmd = messageJson.getString("cmd");
//        String gwID = messageJson.getString("gwID");
//
//        List<PushPartnerBO> uIdList = getPushUsers(gwID,devID,cmd,pushType,uIdMqtt);
//
//        LOGGER.info("push user list is {}", JSON.toJSONString(uIdList));
//
//        return uIdList;
//    }
//
//    /**
//     * 获取推送用户的三方
//     */
//    @Override
//    public List<PushPartnerBO> getPushPartners(List<String> uIdList) throws Exception{
//
//        List<PushPartnerBO> pushPartnerList = new ArrayList<>();
//
//        // 按三方类型进行过滤
//        Map<String,PushPartnerBO> pushPartnerMap = new HashMap<>(16);
//        for (String uId: uIdList){
//
//            // 获取用户三方信息
//            PartnerDetailInfo partnerDetailInfo = getPartnerInfoByUId(uId);
//            if(null == partnerDetailInfo){
//                continue;
//            }
//
//            if(1 == partnerDetailInfo.getPartnerType()){
//                    tidyPushPartnerMap(pushPartnerMap,pushPartnerList,partnerDetailInfo,uId);
//            }
//            else{
//                // 因账号的互通，用户归属的三方和登陆的第三方不同，需要获取当前登陆的三方信息
//                TokenUserInfo userTokenByUid = ssoAPI.getUserTokenByUid(uId);
//                if (partnerDetailInfo.getId() != userTokenByUid.getPartnerId() && userTokenByUid.getPartnerId() != 0) {
//                    partnerDetailInfo = getPartnerInfo(userTokenByUid.getPartnerId());
//                }
//
//                // 封装PushPartner对象，并将三方按三方类型进行分类
//                tidyPushPartnerMap(pushPartnerMap,pushPartnerList,partnerDetailInfo,uId);
//
//                // 获取用户授权的三方信息
//                List<String> partnerIdByUid = oauthUtils.getPartnerIdByUid(uId);
//                for (String oauthPartnerId : partnerIdByUid)
//                {
//                    PartnerDetailInfo oauthPartnerDetailInfo = ssoAPI.getPartnerInfo(oauthPartnerId);
//
//                    // 封装PushPartner对象，并将三方按三方类型进行分类
//                    tidyPushPartnerMap(pushPartnerMap,pushPartnerList,oauthPartnerDetailInfo,uId);
//                }
//            }
//        }
//
//        return pushPartnerList;
//    }
//
//    /**
//     * 发送消息到三方应用
//     */
//    @Override
//    public void send(PushPartnerBO pushPartner, JSONObject messageJson, String sourceMqttTopic, int qos, String msgId)throws Exception{
//        int partnerType = pushPartner.getPartnerDetailInfo().getPartnerType();
//
//        // app三方
//        if (partnerType == PartnerType.PARTNER_APP.getType()) {
//
//            partnerType0Handler.execute(pushPartner, sourceMqttTopic, messageJson.toString(), qos, msgId);
//        }
//
//        // 云平台对接的第三方
//        if (partnerType == PartnerType.PARTNER_CLOUD.getType()) {
//            partnerType1Handler.execute(pushPartner,messageJson.getString(Constants.KEY_USER_ID) ,sourceMqttTopic, messageJson.toString(), qos);
//        }
//
//        // pad逻辑
//        if(partnerType == PartnerType.PARTNER_WEB.getType()) {
//            partnerType2Handler.execute(pushPartner, sourceMqttTopic, messageJson.toString(), qos);
//        }
//
//        // 授权第三方
//        if(partnerType == PartnerType.PARTNER_OAUTH.getType()) {
//            partnerType3Handler.execute(pushPartner, sourceMqttTopic, messageJson.toString(), qos);
//        }
//
//        if(partnerType == 4) {
//            partnerType4Handler.execute(pushPartner,messageJson.getString(Constants.KEY_USER_ID) ,sourceMqttTopic, messageJson.toString(), qos);
//        }
//
//        if (LOGGER.isDebugEnabled()) {
//            LOGGER.debug("message push success.pushPartner:{},messageJson:{}", JSON.toJSONString(pushPartner), messageJson.toJSONString());
//        }
//    }
//
//    /**
//     * 业务后处理
//     *   积分处理
//     */
//    @Override
//    public void postHandle(JSONObject messageJson)throws Exception{
//
//        String targetUser = messageJson.getString(Constants.KEY_USER_ID);
//
//        // 加积分
//        if (pointHandler.isNeedPoint(targetUser,messageJson)) {
//            pointHandler.execute(targetUser, messageJson);
//        }
//
//        if (LOGGER.isDebugEnabled()) {
//            LOGGER.debug("point handle  success.{}", messageJson);
//        }
//    }

//    /**
//     * 将推送三方按三方类型进行分类并封装PushPartner对象
//     */
//    private void tidyPushPartnerMap(Map<String,PushPartnerBO> pushPartnerMap,List<PushPartnerBO> pushPartnerList,PartnerDetailInfo partnerDetailInfo,String uId){
//
//        if(null == partnerDetailInfo || StringUtils.isEmpty(partnerDetailInfo.getPartnerId())){
//
//            return;
//        }
//
//        PushPartnerBO pushPartner = pushPartnerMap.get(partnerDetailInfo.getPartnerId());
//        if(null == pushPartner) {
//            PushPartnerBO tempPushPartner = new PushPartnerBO();
//            List<String> userIds = new ArrayList<>();
//            userIds.add(uId);
//            tempPushPartner.setPartnerDetailInfo(partnerDetailInfo);
//            tempPushPartner.setUsers(userIds);
//            pushPartnerMap.put(partnerDetailInfo.getPartnerId(),tempPushPartner);
//            pushPartnerList.add(pushPartnerMap.get(partnerDetailInfo.getPartnerId()));
//        }
//        else{
//            pushPartner.getUsers().add(uId);
//        }
//    }
//
//    /**
//     * 根据用户获取三方信息
//     */
//    private PartnerDetailInfo getPartnerInfoByUId(String uId) throws Exception {
//        String partnerId = redisService.get(REDIS_KEY_PID_BYUID + uId);
//        if (StringUtils.isEmpty(partnerId)) {
//            UserDetailInfo userDetail = ucAPI.getUserInfo(uId);
//            if (userDetail != null) {
//                redisService.set(REDIS_KEY_PID_BYUID + uId, userDetail.getPartnerId(), 86400);
//                partnerId = String.valueOf(userDetail.getPartnerId());
//            }
//        }
//
//        if (StringUtils.isEmpty(partnerId)) {
//
//        	LOGGER.error("partner of user {} is null.",uId);
//            return null;
//        }
//
//        int pId = Integer.parseInt(partnerId);
//        return getPartnerInfo(pId);
//    }
//
//    /**
//     * 根据三方主键获取三方信息
//     */
//    private PartnerDetailInfo getPartnerInfo(int pId) throws Exception {
//        PartnerDetailInfo partnerDetailInfo = redisService.get(REDIS_KEY_PARTNER_INFO_BYID + pId, PartnerDetailInfo.class);
//        if (partnerDetailInfo == null) {
//            partnerDetailInfo = ssoAPI.getPartnerInfoById(pId);
//            if (partnerDetailInfo != null) {
//                redisService.set(REDIS_KEY_PARTNER_INFO_BYID + pId, partnerDetailInfo, 86400);
//            }
//        }
//
//        if (LOGGER.isDebugEnabled()) {
//            LOGGER.debug("partner is {}，{}.", partnerDetailInfo.getPartnerId(), partnerDetailInfo.getId());
//        }
//
//        return partnerDetailInfo;
//    }
//
//
//    /**
//     * 获取设备关联的用户列表
//     */
//    private List<SimpleUserDevice> getUidsByGatewayId(String gwID, String devID, String cmd) throws Exception {
//        //去DC查找绑定该设备的用户ID
//        UserGrantedUidSimpleInfoData userGrantedUidData = dcAPI.getGrantedUidsSimpleInfo(gwID);
//
//        if (LOGGER.isDebugEnabled()) {
//            LOGGER.debug("query bind and grant user,result={}", JSON.toJSON(userGrantedUidData));
//        }
//
//        List<SimpleUserDevice> uIdList = Collections.synchronizedList(new ArrayList<>());
//        if (userGrantedUidData.getBoundUids() != null) {
//            uIdList.addAll(userGrantedUidData.getBoundUids());
//        }
//        if (userGrantedUidData.getGrantUids() != null) {
//            uIdList.addAll(userGrantedUidData.getGrantUids());
//        }
//        if (!CollectionUtils.isEmpty(userGrantedUidData.getGrantChildDeviceUids())) {
//            if (Constants.CMD_01.equals(cmd) || Constants.CMD_15.equals(cmd) || Constants.CMD_506_ROOM.equals(cmd)
//                    || Constants.CMD_510.equals(cmd) || Constants.CMD_512.equals(cmd)) {
//                uIdList.addAll(userGrantedUidData.getGrantChildDeviceUids());
//            }
//            if (Constants.CMD_500.equals(cmd) || Constants.CMD_501.equals(cmd) || Constants.CMD_502.equals(cmd)
//                    || Constants.CMD_516.equals(cmd)) {
//                // 去DC查找绑定该子设备的用户ID
//                UserGrantedUidSimpleInfoData grantedUidData = dcAPI.getGrantedUidsSimpleInfo(devID);
//
//                if (grantedUidData != null && !CollectionUtils.isEmpty(grantedUidData.getChildDeviceUids())) {
//                    uIdList.addAll(grantedUidData.getChildDeviceUids());
//                }
//            }
//        }
//
//        return uIdList;
//    }
//
//    /**
//     * 获取用户
//     */
//    private List<PushPartnerBO> getPushUsers(String gwID, String devID , String cmd, int pushType, String uIdMqtt)throws Exception {
//
//        // 如果网关id为空则设置为设备id
//        if (StringUtils.isEmpty(gwID)) {
//            gwID = devID;
//        }
//
//        //获取所有用户ID
//        List<SimpleUserDevice> userList = getUidsByGatewayId(gwID, devID, cmd);
//        List<SimpleUserDevice> pushUserList = null;
//
//        //全部转发
//        if (pushType == MQTT_PUSH_ALL) {
//            pushUserList = userList;
//
//        } else if (pushType == MQTT_PUSH_UID_APPOINTED) {
//
//            for (SimpleUserDevice userDeviceVO : userList) {
//                if (userDeviceVO != null && userDeviceVO.getUid().equals(uIdMqtt)) {
//                    pushUserList = new ArrayList<>();
//                    pushUserList.add(userDeviceVO);
//                }
//            }
//        } else if (pushType == MQTT_PUSH_UID_EXCLUSIVE) {
//            //转发除特定用户外的所有用户
//            for (SimpleUserDevice userDeviceVO : userList) {
//                if (userDeviceVO != null && userDeviceVO.getUid().equals(uIdMqtt)) {
//                    userList.remove(userDeviceVO);
//                    break;
//                }
//            }
//
//            pushUserList = userList;
//        } else if (pushType == MQTT_PUSH_UID_DIRECT) {
//            //转发特定用户,不判断用户是否和设备有关联
//            pushUserList = new ArrayList<>();
//            SimpleUserDevice userDeviceVO = new SimpleUserDevice();
//            userDeviceVO.setUid(uIdMqtt);
//            userDeviceVO.setStatus(UserDeviceVO.RELATION_BOUND);
//            pushUserList.add(userDeviceVO);
//        }
//
//        List<PushPartnerBO> pushPartnerList = Collections.synchronizedList(new ArrayList<>());
//
//        // 按三方类型进行过滤
//        Map<String,PushPartnerBO> pushPartnerMap = new ConcurrentHashMap<>(16);
//        Set<String> oauthUserSet = Collections.synchronizedSet(new HashSet<>());
//
//        CountDownLatch countDownLatch = new CountDownLatch(pushUserList.size());
//
//        for (SimpleUserDevice userDeviceVO : pushUserList) {
//            executorService.submit(new PushPartnersQueryRunnable(countDownLatch, partnerService, userDeviceVO, pushPartnerMap, pushPartnerList, cmd, oauthUserSet));
//        }
//
//        countDownLatch.await();
//
//        // 获取oauth的用户
//        getOauthPartner(pushPartnerMap, pushPartnerList, oauthUserSet);
//
//        return pushPartnerList;
//    }
//
//    /**
//     * 安全狗业务逻辑
//     *   获取用户列表
//     *   获取用户的三方信息
//     *   推送消息
//     *   注：设备未上线，此逻辑不会执行
//     */
//    @Override
//    @Deprecated
//    public void safeDogProtocolHanler(String sourceMqttTopic,JSONObject messageJson) throws Exception {
//        String safedogID = sourceMqttTopic.split("/")[3];
//
//        int qos = 0;
//        List<String> uIdList = getUserList4SafeDog(safedogID);
//        List<PushPartnerBO> pushPartnerList = getPushPartners(uIdList);
//
//        LOGGER.info("safedog partner of user is {}", pushPartnerList);
//
//        for(PushPartnerBO pushPartner :pushPartnerList){
//
//            int partnerType = pushPartner.getPartnerDetailInfo().getPartnerType();
//
//            // 如果是app或pad
//            if (partnerType == 0 ) {
//                String msgId = DigestUtils.md5Hex(messageJson.toString());
//                partnerType0Handler.execute(pushPartner, sourceMqttTopic, messageJson.toString(), qos, msgId);
//            }
//        }
//    }
//
//    @Override
//    public void wlinkProtocolHanler(String sourceMqttTopic, JSONObject messageJson) throws Exception {
//        ProtocolBO bo = messageJson.toJavaObject(ProtocolBO.class);
//
//        String receiver = bo.getReceiver();
//
//        if (sourceMqttTopic.endsWith("notice")) {
//
//            if (receiver.contains(",")) {
//                String[] uIds = receiver.split(",");
//
//                for (String uId : uIds) {
//                    bo.setReceiver(uId);
//                    partnerWlinkHandler.sendNoticeToUser(bo);
//                }
//            } else {
//                partnerWlinkHandler.sendNoticeToUser(bo);
//            }
//        } else if (sourceMqttTopic.endsWith("push")) {
//
//            if (receiver.contains(",")) {
//                String[] uIds = receiver.split(",");
//
//                for (String uId : uIds) {
//                    bo.setReceiver(uId);
//                    partnerWlinkHandler.sendPushToUser(bo);
//                }
//            } else {
//                partnerWlinkHandler.sendPushToUser(bo);
//            }
//        } else if (StringUtils.isEmpty(receiver))
//        {
//            partnerWlinkHandler.sendToAllUser(bo);
//
//            // 判断是否网关上报的属性状态信息
//            if (bo.getBehavior() == BehaviorTypeEnums.BEHAVIOR_CLIENT_STATE.getBehavior()) {
//
//                List<Object> objArr = (List<Object>) bo.getBody();
//
//                Map<String, Object> bodyMap = (Map<String, Object>) objArr.get(0);
//                int state = Integer.parseInt(bodyMap.get("state").toString());
//
//                //如果uuid不为空，则表示是设备的状态
//                if (StringUtils.isEmpty(bo.getUuid())) {
//                    if (state == DeviceStateEnums.UPLINE.getValue()) {
//                        String key = "UAS#WLINK#CREATE#HOUSE#JUDGE" + bo.getSender();
//                        if (redisService.containsKey(key)) {
//                            bo.setReceiver(redisService.get(key));
//                            partnerWlinkHandler.sendPushToUser(bo);
//
//                            redisService.delete(key);
//                        }
//                    }
//                }
//            }
//        }
//        else
//        {
//            if (receiver.contains(",")) {
//                String[] uIds = receiver.split(",");
//
//                for (String uId : uIds) {
//                    bo.setReceiver(uId);
//                    partnerWlinkHandler.sendToUser(bo);
//                }
//            } else {
//                partnerWlinkHandler.sendToUser(bo);
//            }
//
//        }
//    }
//
//    /**
//     * 获取安全狗的用户列表
//     */
//    private List<String> getUserList4SafeDog(String safedogID) throws Exception{
//        GrantUidsParam dcQueryParam = new GrantUidsParam();
//        dcQueryParam.setDeviceId(safedogID);
//        //去DC查找绑定该设备的用户ID
//        UserGrantedUidData userGrantedUidData = dcAPI.getGrantedUids(dcQueryParam);
//
//        if (LOGGER.isDebugEnabled()) {
//            LOGGER.debug("query bind and grant user,result={}", JSON.toJSON(userGrantedUidData));
//        }
//
//        List<String> uIdList = new ArrayList<>();
//        if (userGrantedUidData.getBoundUids() != null) {
//            uIdList.addAll(userGrantedUidData.getBoundUids());
//        }
//        if (userGrantedUidData.getGrantUids() != null) {
//            uIdList.addAll(userGrantedUidData.getGrantUids());
//        }
//        return uIdList;
//    }
//
//    private void getOauthPartner(Map<String,PushPartnerBO> pushPartnerMap, List<PushPartnerBO> pushPartnerList, Set<String> uIds) throws Exception {
//        if (uIds.isEmpty()) {
//            return;
//        }
//
//        // 从缓存中查找用户对应的授权信息
//        getOauthPartnerByCache(pushPartnerMap, pushPartnerList, uIds);
//
//        // 以下調用次數較少，不考慮性能問題
//        if (uIds.isEmpty()) {
//            return;
//        }
//
//        // 没有从缓存中查询到授权信息的用户到oauth服务查询，将数据缓存
//        getOauthPartnerByOauthServer(pushPartnerMap, pushPartnerList, uIds);
//
//        // 没有授权的用户缓存空数据
//        handleNoOauthUser(uIds);
//    }
//
//    /**
//     * 从缓存中查找用户对应的授权信息
//     */
//    private void getOauthPartnerByCache(Map<String,PushPartnerBO> pushPartnerMap, List<PushPartnerBO> pushPartnerList, Set<String> uIds) throws Exception {
//
//        Iterator<String> it = uIds.iterator();
//        while (it.hasNext()) {
//            String uId = it.next();
//            String userPartnerStr = redisService.get(REDIS_KEY_OAUTH_UID_PARTNER + uId);
//
//            if (userPartnerStr == null) {
//                continue;
//            }
//
//            UserPartner tempPartnerUser = JSONObject.parseObject(userPartnerStr, UserPartner.class);
//            List<String> clientIds = tempPartnerUser.getClientIds();
//
//            if (clientIds == null) {
//                continue;
//            }
//
//            for (String clientId : clientIds) {
//                if (StringUtils.isEmpty(clientId)) {
//                    continue;
//                }
//
//                PartnerDetailInfo oauthPartnerDetailInfo = ssoAPI.getPartnerInfo(clientId);
//
//                // 封装PushPartner对象，并将三方按三方类型进行分类
//                tidyPushPartnerMap(pushPartnerMap, pushPartnerList, oauthPartnerDetailInfo, uId);
//            }
//
//            it.remove();
//        }
//    }
//
//    /**
//     * 没有从缓存中查询到授权信息的用户到oauth服务查询，将数据缓存
//     */
//    private void getOauthPartnerByOauthServer(Map<String,PushPartnerBO> pushPartnerMap, List<PushPartnerBO> pushPartnerList, Set<String> uIds) throws Exception {
//        List<PartnerUser> partnerUsers = oauthUtils.batchGetPartnerIdByUid(uIds);
//        Map<String, UserPartner> userPartnerMap = new HashMap<>(16);
//
//        for (PartnerUser partnerUser : partnerUsers) {
//            String partnerId = partnerUser.getClientId();
//            String uId = partnerUser.getUserName();
//
//            PartnerDetailInfo oauthPartnerDetailInfo = ssoAPI.getPartnerInfo(partnerId);
//
//            // 封装PushPartner对象，并将三方按三方类型进行分类
//            tidyPushPartnerMap(pushPartnerMap, pushPartnerList, oauthPartnerDetailInfo, uId);
//
//            if (userPartnerMap.containsKey(uId)) {
//                userPartnerMap.get(uId).getClientIds().add(partnerId);
//            } else {
//                List<String> clientIds = new ArrayList<>();
//                clientIds.add(partnerId);
//
//                UserPartner userPartner = new UserPartner();
//                userPartner.setUserName(uId);
//                userPartner.setClientIds(clientIds);
//
//                userPartnerMap.put(uId, userPartner);
//            }
//        }
//
//        for (Map.Entry<String, UserPartner> userPartner : userPartnerMap.entrySet()) {
//            String uId = userPartner.getKey();
//            redisService.set(REDIS_KEY_OAUTH_UID_PARTNER + uId, JSON.toJSONString(userPartner.getValue()), CommonConstants.ONE_WEEK_SECOND);
//            uIds.remove(uId);
//        }
//    }
//
//    /**
//     * 没有授权的用户缓存空数据
//     */
//    private void handleNoOauthUser(Set<String> uIds) {
//        Iterator<String> it = uIds.iterator();
//        while (it.hasNext()) {
//            String uId = it.next();
//            UserPartner userPartner = new UserPartner();
//            userPartner.setUserName(uId);
//            userPartner.setClientIds(new ArrayList<>());
//
//            redisService.set(REDIS_KEY_OAUTH_UID_PARTNER + uId, userPartner, CommonConstants.ONE_WEEK_SECOND);
//        }
//    }
}
