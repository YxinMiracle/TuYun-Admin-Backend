package com.yxinmiracle.user.service;

import com.yxinmiracle.model.common.dtos.ResponseResult;
import com.yxinmiracle.model.user.dtos.HandleRoleDto;

public interface RoleService {
    ResponseResult getRoleList();

    ResponseResult addRole(HandleRoleDto dto);

    ResponseResult editRole(HandleRoleDto dto);
}
