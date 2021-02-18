package com.ict.login.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * sys_permission
 * @author 
 */
@Data
public class Permission implements Serializable {
    /**
     * 主键id
     */
    private Integer id;

    /**
     * 权限code
     */
    private String permissionCode;

    /**
     * 权限名
     */
    private String permissionName;

    private static final long serialVersionUID = 1L;
}