package com.ict.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * ict_dict
 * @author 
 */
@Data
public class IctDict implements Serializable {
    /**
     * id
     */
    private Integer id;

    /**
     * 字典编码
     */
    private String dictCode;

    /**
     * 字典名称
     */
    private String dictName;

    /**
     * parent_id
     */
    private Integer parentId;

    /**
     * 是否删除 0 未删除  1已删除
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private Integer createUserId;

    /**
     * 是否有子节点 0 否 1 是 ，默认0
     */
    private Integer isChildren;

    private static final long serialVersionUID = 1L;
}