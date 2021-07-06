/**
 * Copyright (C) 2006-2017 Wulian All rights reserved
 */
package com.ict.common;


public class Constants {
    /**
     * rabbitmq的队列名称,APP上报过来的消息全部在这个队列之后,后续可以考虑分发到多个队列
     */
    public static final String RABBIT_QUEUE_APP_UPLOAD_DATA = "app_upload_data";

    public static final String RABBIT_QUEUE_DEVICE_UPLOAD_DATA_BECK = "device_upload_data_beck";

    public static final String RABBIT_QUEUE_DEVICE_UPLOAD_DATA_BECK_RESPONSE = "device_upload_data_beck_response";

    public static final String RABBIT_QUEUE_UPSTREAM_MSG_TO_UAS = "upstream-data-touas";

    public static final String RABBIT_MSG_ATTR_TPPID = "X-upstream-tppid";

    public static final String RABBIT_MSG_ATTR_TOPIC = "X-upstream-topic";

    public static final String RABBIT_MSG_ATTR_SYNC_FLAG = "X-upstream-sync-flag";

    public static final String MSG_TOPIC = "ADD_sourcetopic";
    public static final String MSG_PARTNERID = "ADD_partnerid";

    /**
     * 默认设备名占位符
     */
    public static final String DEFAULT_DEVICE_NAME_PLACEHOLDER = "#$default$#";

    /**
     * Content-Type
     */
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String APPLICATION_JSON = "application/json;charset=utf-8";

    /**
     * 网关上线
     */
    public static final String CMD_01 = "01";

    /**
     * 网关下线
     */
    public static final String CMD_15 = "15";

    /**
     * 设备信息上报
     */
    public static final String CMD_500 = "500";

    /**
     * 控制设备
     */
    public static final String CMD_501 = "501";

    /**
     *  读取场景列表
     */
    public static final String CMD_504_SCENE = "504";

    /**
     * 读取区域列表
     */
    public static final String CMD_506_ROOM = "506";

    /**
     * 修改密码
     */
    public static final String CMD_509_CHANGE_PASSWORD = "509";

    /**
     * 获取设备列表
     */
    public static final String CMD_510 = "510";

    /**
     * 请求与设置网关信息
     */
    public static final String CMD_512 = "512";

    /**
     * 设置设备信息(区域名字删除设备)
     */
    public static final String CMD_502 = "502";

    /** 华尔思背景音乐系统 */
	public static final String CMD_517 = "517";

	/** 全角度红外按键码增删改查 */
	public static final String CMD_516 = "516";

    /**
     * 设置区域信息
     */
    public static final String CMD_505 = "505";

    /**
     * 设置场景开关绑定信息_513
     */
    public static final String CMD_513 = "513";

    /**
     * 读取场景开关绑定信息_514
     */
    public static final String CMD_514 = "514";

    /**
     * 管家操作_507
     */
    public static final String CMD_507 = "507";

    /**
     * 爱看绑定成功_bindDevice
     */
    public static final String CMD_BIND_DEVICE = "bindDevice";

    /**
     * 网关和用户的分享关系，网关级别
     */
    public static final int GW_GRANT_RELATION_2 = 2;

    /**
     * 网关和用户的分享关系，子设备级别
     */
    public static final int GW_GRANT_RELATION_3 = 3;

    /**
     * 网关和用户的关系，绑定关系
     */
    public static final int GW_BIND_RELATION_1 = 1;

    // partnerId="wulian_app"
    public static final String WULIAN_APP = "wulian_app";

    // 请求头partnerId
    public static final String HEADER_PARTNER_ID = "WL-PARTNER-ID";

    public static final String UAS_SYNC_MSG_QUEUE = "UAS_SYNC_MSG_QUEUE";

    public static final String KEY_CMD = "cmd";

    public static final String KEY_UID = "uId";

    public static final String KEY_MODE = "mode";

    public static final String KEY_ACTION = "action";

    public static final String KEY_DEVICE = "device";

    public static final String KEY_ROOM = "room";

    public static final String KEY_ROOM_ID = "roomId";

    public static final String KEY_ROOM_NAME = "roomName";

    public static final String KEY_TOP_DEVICE_ID = "topDeviceId";

    public static final String KEY_USER_ID = "userID";

    public static final String KEY_TYPE = "type";

    public static final String KEY_GW_ID = "gwID";

    public static final String KEY_GW_PWD = "gwPwd";

    /** 智尚一路开关 **/
    public static final String TYPE_ZHISHANG_1 = "Am";

    /** 智尚一路开关 **/
    public static final String TYPE_ZHISHANG_2 = "An";

    /** 智尚一路开关 **/
    public static final String TYPE_ZHISHANG_3 = "Ao";

    /** 安徽移动 **/
    public static final String PARTNER_ID_ANHUI_CMCC = "fd23eae7ddef6de9";

    /** 锐吉（魔镜） **/
    public static final String PARTNER_ID_RUIJI = "66cc0764d0028564";

    /** 权限状态，允许 **/
    public static final int PERMISSION_STATUS_PERMIT = 1;

    /** 权限状态，禁止 **/
    public static final int PERMISSION_STATUS_FORBID = 0;

    /** 权限对接类型，接口 **/
    public static final int PERMISSION_TYPE_INTERFACE = 0;

    /** 权限对接类型，命令 **/
    public static final int PERMISSION_TYPE_COMMAND = 1;

    /** 权限对接类型，设备 **/
    public static final int PERMISSION_TYPE_DEVICE = 2;

    /** 权限用户类型，第三方  **/
    public static final int PERMISSION_USER_TYPE_THIRD_PARTY = 0;

    /** 权限用户类型，用户 **/
    public static final int PERMISSION_USER_TYPE_USER = 1;

    /** 权限限制情况，命令  **/
    public static final int PERMISSION_FORBID_TYPE_COMMAND = 1;

    /** 权限限制情况，设备-网关 **/
    public static final int PERMISSION_FORBID_TYPE_DEVICE_COORDINATOR = 2;

    /** 权限限制情况，设备-子设备 **/
    public static final int PERMISSION_FORBID_TYPE_DEVICE_DEVICE = 3;
}
