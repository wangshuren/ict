package com.ict.service;

import com.ict.param.response.TUserResponse;

public interface IUserService {
    TUserResponse getByUserId(Integer userId);
}
