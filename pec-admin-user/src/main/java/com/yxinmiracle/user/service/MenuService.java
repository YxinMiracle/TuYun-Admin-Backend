package com.yxinmiracle.user.service;

import com.yxinmiracle.model.common.dtos.ResponseResult;
import com.yxinmiracle.model.user.dtos.RoleMenuDto;

public interface MenuService {
    ResponseResult getMenuList();

    ResponseResult getMenuListByRoleId(Integer roleId);

    ResponseResult updateRoleMenu(RoleMenuDto dto);
}
