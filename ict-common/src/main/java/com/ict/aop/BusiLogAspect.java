package com.ict.aop;

import com.ict.annotation.BusiLogAnno;
import com.ict.service.LogRemService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * @Description: 业务日志
 * @Author wangsr
 * @Date 2021/5/10
 * @Version 1.0
 */
@Aspect
@Component
public class BusiLogAspect {
    private static final Logger log = LoggerFactory.getLogger(BusiLogAspect.class);

    @Autowired
    private LogRemService logRemService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @AfterReturning(value = "@annotation(busiLog)")
    public void saveUserLog(JoinPoint joinPoit, BusiLogAnno busiLog) throws Exception {

        try {
            com.ict.entity.model.BusiLog logrem = new com.ict.entity.model.BusiLog();
            String token = httpServletRequest.getHeader("token");

            logrem.setBusiOp(busiLog.busiOp());
            logrem.setCreateUserId(1);
            logrem.setOpUserId(1);
            logrem.setOpUserName("admin");
            logrem.setStatus(1);
            logrem.setId(UUID.randomUUID().toString());
            log.info("开始插入日志");//输出到tomcat日志，后续有问题，查找方便
            logRemService.operatorBusiLog(logrem);//插入到log 日志表
        } catch (Exception e) {
            log.info("哈哈哈，错了吧");
        }

    }

    @AfterThrowing(value = "@annotation(busiLog)")
    public void saveUserLog2(JoinPoint joinPoit, BusiLogAnno busiLog) throws Exception {

        try {
            com.ict.entity.model.BusiLog logrem = new com.ict.entity.model.BusiLog();
            String token = httpServletRequest.getHeader("token");
            httpServletRequest.setAttribute("logId", "12545454545454545454");

            logrem.setBusiOp(busiLog.busiOp());
            logrem.setCreateUserId(1);
            logrem.setOpUserId(1);
            logrem.setOpUserName("admin");
            logrem.setStatus(0);
            log.info("开始插入日志");//输出到tomcat日志，后续有问题，查找方便
            logRemService.operatorBusiLog(logrem);//插入到log 日志表
        } catch (Exception e) {
            log.info("哈哈哈，错了吧");
        }

    }


}
