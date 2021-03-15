package com.ict.ictsamplemanage.controller;

import com.ict.ictsamplemanage.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserServiceImpl userService;

    @Value("${test.username}")
    private String username;

    @GetMapping("/userInfo")
    public Map<String, Object> userVa() throws Exception {
        System.out.println(username);
        return userService.getUserInfo(1);
    }

    @GetMapping("/userInfo2")
    public String userVa2() throws Exception {
        return "success";
    }
}
