package com.ict.ictsamplemanage.controller;

import com.ict.ictsamplemanage.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserServiceImpl userService;

    @GetMapping("/userInfo")
    public Map<String, Object> userVa() throws Exception {
        return userService.getUserInfo(1);
    }
}
