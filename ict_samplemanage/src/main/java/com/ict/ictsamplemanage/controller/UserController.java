package com.ict.ictsamplemanage.controller;

import com.ict.ictsamplemanage.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/user")
@RefreshScope
public class UserController {
    @Autowired
    private UserServiceImpl userService;

    @Value("${test.username}")
    private String username;

    @Value("${test.username2}")
    private String username2;

    @Value("${test.username3}")
    private String username3;


    @GetMapping("/userInfo")
    public Map<String, Object> userVa() throws Exception {
        System.out.println(username);
        System.out.println(username2);
        System.out.println(username3);
        return userService.getUserInfo(1);
    }

    @GetMapping("/userInfo2")
    public String userVa2() throws Exception {
        return "success";
    }
}
