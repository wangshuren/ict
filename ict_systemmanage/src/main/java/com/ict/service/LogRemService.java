package com.ict.service;

import com.ict.entity.model.BusiLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @Author wangsr
 * @Date 2021/5/10
 * @Version 1.0
 */
@Service
public class LogRemService {
    @Autowired
    private BusiLogDao busiLogDao;

    public String operatorBusiLog(BusiLog busiLog) {
        busiLogDao.insert(busiLog);
        return "新增成功";
    }
}
