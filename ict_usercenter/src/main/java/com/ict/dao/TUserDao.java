package com.ict.dao;

import com.ict.entity.TUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TUserDao {
    int deleteByPrimaryKey(@Param("userId") Integer userId);

    int insert(TUser record);

    int insertSelective(TUser record);

    TUser selectByPrimaryKey(Integer userId);

    int updateByPrimaryKeySelective(TUser record);

    int updateByPrimaryKey(TUser record);
}