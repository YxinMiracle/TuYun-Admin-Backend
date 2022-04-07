package com.yxinmiracle.user.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yxinmiracle.model.common.dtos.ResponseResult;
import com.yxinmiracle.model.common.enums.AppHttpCodeEnum;
import com.yxinmiracle.model.user.dtos.RoleMenuDto;
import com.yxinmiracle.model.user.pojos.Menu;
import com.yxinmiracle.model.user.pojos.RoleMenu;
import com.yxinmiracle.user.mapper.MenuMapper;
import com.yxinmiracle.user.mapper.RoleMenuMapper;
import com.yxinmiracle.user.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class MenuServiceImpl implements MenuService {
    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Override
    public ResponseResult getMenuList() {
        List<Menu> menus = menuMapper.selectList(null);
        return ResponseResult.okResult(menus);
    }

    @Override
    public ResponseResult getMenuListByRoleId(Integer roleId) {
        if (roleId == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        List<Menu> menuList = menuMapper.getMenuListByRoleId(roleId);
        return ResponseResult.okResult(menuList);
    }

    @Override
    public ResponseResult updateRoleMenu(RoleMenuDto dto) {
        if (Objects.isNull(dto)){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        Integer roleId = dto.getRoleId();
        roleMenuMapper.delete(Wrappers.<RoleMenu>lambdaQuery().eq(RoleMenu::getRoleId,roleId)); // 删除roleMenu表中roleId==roleId的数据
        for (Integer menuId : dto.getMenuIdList()) {
            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setMenuId(menuId);
            roleMenu.setRoleId(roleId);
            roleMenuMapper.insert(roleMenu);
        }
        return ResponseResult.okResult();
    }
}
