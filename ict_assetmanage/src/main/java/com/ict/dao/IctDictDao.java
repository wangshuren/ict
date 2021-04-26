package com.ict.dao;

import com.ict.entity.IctDict;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IctDictDao {
    int deleteByPrimaryKey(Integer id);

    int insert(IctDict record);

    int insertSelective(IctDict record);

    IctDict selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(IctDict record);

    int updateByPrimaryKey(IctDict record);

    List<IctDict> selectDicts();
}