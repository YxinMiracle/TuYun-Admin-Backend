package com.yxinmiracle.apis.user;

import com.yxinmiracle.model.common.dtos.ResponseResult;
import com.yxinmiracle.model.user.dtos.PageUserDto;
import com.yxinmiracle.model.user.dtos.UserDto;
import com.yxinmiracle.model.user.pojos.User;

public interface UserControllerApi {
    public ResponseResult login(User user);

    public ResponseResult addUser(UserDto dto);

    /**
     * 获取分页的user数据
     * @param dto
     * @return
     */
    public ResponseResult getUser(PageUserDto dto);

    /**
     * 更新user数据
     * @param dto
     * @return
     */
    public ResponseResult updateUser(UserDto dto);

    /**
     * 删除用户的数据
     * @param userId
     * @return
     */
    public ResponseResult deleteUser(String userId);

    public ResponseResult getUserByServicesId(Integer servicesId);
}
