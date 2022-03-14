package com.yxinmiracle.user.service;

import com.yxinmiracle.model.common.dtos.ResponseResult;
import com.yxinmiracle.model.user.dtos.PageUserDto;
import com.yxinmiracle.model.user.dtos.UserDto;
import com.yxinmiracle.model.user.pojos.User;

public interface UserService {
    ResponseResult login(User user);

    ResponseResult getUser(PageUserDto dto);

    ResponseResult addUser(UserDto dto);

    ResponseResult updateUser(UserDto dto);

    ResponseResult deleteUser(String userId);

    ResponseResult getUserByServicesId(Integer servicesId);
}
