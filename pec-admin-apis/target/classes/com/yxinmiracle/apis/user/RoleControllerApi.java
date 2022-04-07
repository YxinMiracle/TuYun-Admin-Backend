package com.yxinmiracle.apis.user;

import com.yxinmiracle.model.common.dtos.ResponseResult;
import com.yxinmiracle.model.user.dtos.HandleRoleDto;

public interface RoleControllerApi {
    public ResponseResult getRoleList();

    public ResponseResult addRole(HandleRoleDto dto);

    public ResponseResult editRole(HandleRoleDto dto);
}
