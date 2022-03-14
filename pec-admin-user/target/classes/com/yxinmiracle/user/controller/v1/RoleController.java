package com.yxinmiracle.user.controller.v1;

import com.yxinmiracle.apis.user.RoleControllerApi;
import com.yxinmiracle.model.common.dtos.ResponseResult;
import com.yxinmiracle.user.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
