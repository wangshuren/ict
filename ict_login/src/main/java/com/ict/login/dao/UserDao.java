package com.ict.login.dao;

import com.ict.login.entity.Permission;
import com.ict.login.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserDao {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    /**
     * 根据用户名查询用户
     *
     * @param userName
     * @return
     */
    User selectByName(@Param("userName") String userName);

    /**
     * 查询用户的权限列表
     *
     * @param userId
     * @return
     */
    List<Permission> selectListByUser(@Param("userId") Integer userId);
}