package com.yxinmiracle.user.controller.v1;

import com.yxinmiracle.apis.user.RoleControllerApi;
import com.yxinmiracle.model.common.dtos.ResponseResult;
import com.yxinmiracle.model.user.dtos.HandleRoleDto;
import com.yxinmiracle.user.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/role")
public class RoleController implements RoleControllerApi {
    @Autowired
    private RoleService roleService;

    @GetMapping("/list")
    @Override
    public ResponseResult getRoleList() {
        return roleService.getRoleList();
    }

    @Override
    @RequestMapping(value = "/handel",method = RequestMethod.POST)
    public ResponseResult addRole(@RequestBody HandleRoleDto dto) {
        return roleService.addRole(dto);
    }

    @Override
    @RequestMapping(value = "/handel",method = RequestMethod.PUT)
    public ResponseResult editRole(@RequestBody HandleRoleDto dto) {
        return roleService.editRole(dto);
    }



}
