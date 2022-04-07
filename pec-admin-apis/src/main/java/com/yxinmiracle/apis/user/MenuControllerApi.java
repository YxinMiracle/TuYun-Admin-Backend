package com.yxinmiracle.apis.user;

import com.yxinmiracle.model.common.dtos.ResponseResult;
import com.yxinmiracle.model.user.dtos.RoleMenuDto;

public interface MenuControllerApi {

    /**
     * 获取全部的menu
     * @return
     */
    public ResponseResult getMenuList();

    public ResponseResult getMenuListByRoleId(Integer roleId);

    /**
     * 更新roleMenu表
     * @param dto
     * @return
     */
    public ResponseResult updateRoleMenu(RoleMenuDto dto);

}
