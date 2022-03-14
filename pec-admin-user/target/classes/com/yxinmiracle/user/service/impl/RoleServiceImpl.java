package com.yxinmiracle.user.service.impl;

import com.yxinmiracle.model.common.dtos.ResponseResult;
import com.yxinmiracle.model.user.pojos.Role;
import com.yxinmiracle.user.mapper.RoleMapper;
import com.yxinmiracle.user.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public ResponseResult getRoleList() {
        List<Role> roles = roleMapper.selectList(null);
        return ResponseResult.okResult(roles);
    }
}
