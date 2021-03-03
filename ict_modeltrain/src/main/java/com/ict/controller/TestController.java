package com.ict.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: 测试
 * @Author wangsr
 * @Date 2021/3/3
 * @Version 1.0
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @RequestMapping("/sou")
    public void test() {
        System.out.println("this is a test");
    }
}