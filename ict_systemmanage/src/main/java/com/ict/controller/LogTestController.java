package com.ict.controller;

import com.ict.annotation.BusiLogAnno;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author wangsr
 * @Date 2021/5/10
 * @Version 1.0
 */
@RestController
@RequestMapping("/log")
public class LogTestController {

    @GetMapping("/test")
    @BusiLogAnno(busiName="用户权限", busiOp="修改", remarkClass=LogTestController.class)
    public void testLog() {
        int a = 1/0;
        System.out.println("日志测试");
    }
}
