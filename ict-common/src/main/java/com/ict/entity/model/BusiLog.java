package com.ict.entity.model;

import lombok.Data;

/**
 * @Description:
 * @Author wangsr
 * @Date 2021/5/10
 * @Version 1.0
 */
@Data
public class BusiLog {
    private String id;

    private Integer opUserId;

    private String opUserName;

    private String busiName;

    private String busiOp;

    private Integer status;

    private String createTime;

    private Integer createUserId;
}
