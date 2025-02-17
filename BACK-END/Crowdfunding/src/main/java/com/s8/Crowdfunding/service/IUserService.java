package com.s8.Crowdfunding.service;

import com.s8.Crowdfunding.dto.UserRequest;
import com.s8.Crowdfunding.model.Users;

public interface IUserService {
    public Users createUser(UserRequest userDto);
}
