package com.ict.service.impl;

import com.ict.dao.TUserDao;
import com.ict.entity.TUser;
import com.ict.param.response.TUserResponse;
import com.ict.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements IUserService {
    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private TUserDao tUserDao;

    @Override
    public TUserResponse getByUserId(Integer userId) {
        TUserResponse resp = new TUserResponse();

        TUser user = tUserDao.selectByPrimaryKey(userId);
        if (user != null) {
            resp.setPassword(user.getPassword());
            resp.setPhone(user.getPhone());
            resp.setUserId(user.getUserId());
            resp.setUsername(user.getUsername());
        }

        return resp;
    }
}
