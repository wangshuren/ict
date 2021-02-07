package com.ict.controller;

import com.ict.param.response.TUserResponse;
import com.ict.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户服务
 *
 * @author wsr
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @GetMapping("/user/info")
    public TUserResponse getUserInfo(@RequestParam("userId")Integer userId) {
        return userService.getByUserId(userId);
    }
}