package com.ict.base;

import lombok.Data;

/**
 * @Description: 公共返回类
 * @Author wangsr
 * @Date 2021/4/26
 * @Version 1.0
 */
@Data
public class BaseResponse<T> {
    /**
     * 返回码
     */
    protected String code;

    /**
     * 消息
     */
    protected String msg;

    /**
     * 返回对象
     */
    protected T data;

    public BaseResponse() {
    }

    public BaseResponse(String code) {
        this.code = code;
    }

    public BaseResponse(String code, T data) {
        this.code = code;
        this.data = data;
    }

    public BaseResponse(String code, String message) {
        this.code = code;
        this.msg = message;
    }

    public BaseResponse(String code, String message, T data) {
        this.code = code;
        this.msg = message;
        this.data = data;
    }
}