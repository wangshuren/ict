package com.ict.ictsamplemanage.service.impl;

import com.ict.api.user.RemoteUsercenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserServiceImpl  {
    @Autowired
    private RemoteUsercenterService remoteUsercenterService;

    public Map<String, Object> getUserInfo(Integer userId) throws Exception {
        Map<String, Object> map =  remoteUsercenterService.getUserInfo(userId + "");

        return map;
    }
}
