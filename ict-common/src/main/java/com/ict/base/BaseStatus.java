package com.ict.base;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Author wangsr
 * @Date 2021/4/26
 * @Version 1.0
 */
public enum BaseStatus {
    SUCCESS("20000", "成功"),
    UNKNOWN_ERROR("2000001", "发生未知错误"),
    DUPLICATE_OPERATE("2000002", "重复操作"),
    RECORD_NOT_FOUND("2000004", "记录不存在"),
    PARAM_ERROR("2000005", "参数错误"),
    PARAMETER_IS_NULL("2000007", "请求参数为空"),
    VALIDATION_FAILED("2000010","用户名或密码错误"),
    CODE_500("500", "500内部错误");
    private String code;
    private String message;

    BaseStatus(String code, String message) {
        this.code = code;
        this.message = message;
    }

    private static final Map<String, BaseStatus> intToEnum = new HashMap<>();

    static {
        for (BaseStatus p : values()) {
            intToEnum.put(p.getCode(), p);
        }
    }

    public static BaseStatus fromInt(Integer type) {
        try {
            return intToEnum.get(type);
        } catch (Exception e) {
            return UNKNOWN_ERROR;
        }
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}