package com.ict.controller;

import com.ict.annotation.BusiLogAnno;
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

    // 用gateway调用地址为http://localhost:9201/usercenter/user/user/info?userId=1
    @GetMapping("/user/info")
    @BusiLogAnno(busiOp = "测试2",busiName = "测试1")
    public TUserResponse getUserInfo(@RequestParam("userId")Integer userId) {
        return userService.getByUserId(userId);
    }
}
