package com.ict.login.dao;

import com.ict.login.entity.Permission;
import com.ict.login.entity.SysPermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description:
 * @Author wangsr
 * @Date 2021/2/18
 * @Version 1.0
 */
@Mapper
public interface SysPermissionDao {
    List<SysPermission> selectListByPath(@Param("url")String url);
}