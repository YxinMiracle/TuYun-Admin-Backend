package com.yxinmiracle.user.controller.v1;

import com.yxinmiracle.apis.user.MenuControllerApi;
import com.yxinmiracle.model.common.dtos.ResponseResult;
import com.yxinmiracle.model.user.dtos.RoleMenuDto;
import com.yxinmiracle.user.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/menu")
public class MenuController implements MenuControllerApi {

    @Autowired
    private MenuService menuService;

    @GetMapping("/list")
    @Override
    public ResponseResult getMenuList() {
        return menuService.getMenuList();
    }

    @GetMapping("/list/{roleId}")
    @Override
    public ResponseResult getMenuListByRoleId(@PathVariable("roleId") Integer roleId) {
        return menuService.getMenuListByRoleId(roleId);
    }

    @Override
    @PostMapping("/add/roleMenu")
    public ResponseResult updateRoleMenu(@RequestBody RoleMenuDto dto) {
        return menuService.updateRoleMenu(dto);
    }
}
