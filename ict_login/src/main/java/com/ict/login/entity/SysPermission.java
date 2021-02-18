package com.ict.login.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description:
 * @Author wangsr
 * @Date 2021/2/18
 * @Version 1.0
 */
@Data
public class SysPermission implements Serializable {
    private static final long serialVersionUID = -7383530602278105733L;

    private String url;

    private String description;

    private Integer permissionId;

    private String permissionCode;

    private String permissionName;
}