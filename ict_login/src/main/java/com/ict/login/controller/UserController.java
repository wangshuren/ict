package com.ict.login.controller;

import com.ict.login.common.JsonResult;
import com.ict.login.common.ResultTool;
import com.ict.login.dao.UserDao;
import com.ict.login.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Description: 用户
 * @Author wangsr
 * @Date 2021/2/18
 * @Version 1.0
 */

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserDao userDao;


    @GetMapping("/getUser")
    @PreAuthorize("hasAuthority('CHECKITEM_QUERY')")
    public JsonResult getUser() {
        User users = userDao.selectByName("1555");
        return ResultTool.success(users);
    }
    @GetMapping("/test")
    public JsonResult test() {
        return ResultTool.success("hello world");
    }
}