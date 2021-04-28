package com.ict.entity.model;

import lombok.Data;

import java.util.Date;

/**
 * @Description:
 * @Author wangsr
 * @Date 2021/4/27
 * @Version 1.0
 */
@Data
public class User {

    private Integer id;

    private String token;

    /**
     * 名称
     */
    private String userName;

    /**
     * 密码
     */
    private String password;

    /**
     * 手机
     */
    private String phoneNumber;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}