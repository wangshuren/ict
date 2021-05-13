package com.ict.dao;

import com.ict.annotation.SysLogAnno;
import com.ict.entity.model.BusiLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BusiLogDao {
    int deleteByPrimaryKey(Integer id);

    @SysLogAnno(oprName="新增")
    int insert(BusiLog record);

    int insertSelective(BusiLog record);

    BusiLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BusiLog record);

    int updateByPrimaryKey(BusiLog record);
}