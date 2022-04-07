package com.yxinmiracle.user.service.impl;

import com.yxinmiracle.model.common.dtos.ResponseResult;
import com.yxinmiracle.model.common.enums.AppHttpCodeEnum;
import com.yxinmiracle.model.user.dtos.HandleRoleDto;
import com.yxinmiracle.model.user.pojos.Role;
import com.yxinmiracle.user.mapper.RoleMapper;
import com.yxinmiracle.user.service.RoleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public ResponseResult getRoleList() {
        List<Role> roles = roleMapper.selectList(null);
        return ResponseResult.okResult(roles);
    }

    @Override
    public ResponseResult addRole(HandleRoleDto dto) {
        if (Objects.isNull(dto)){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        if (StringUtils.isBlank(dto.getRoleName()) || dto.getRoleName().length()>40){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        if (StringUtils.isBlank(dto.getRemark()) || dto.getRemark().length()>40){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        /**
         * 取出 userId
         */
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        Integer userId = (Integer) authentication.getPrincipal();

        Role role = new Role();
        role.setColor(dto.getColor());
        role.setRemark(dto.getRemark());
        role.setRoleName(dto.getRoleName());
        role.setRoleKey(role.getRoleName());
        role.setCreateBy(userId);
        role.setCreateTime(new Date());
        role.setUpdateBy(userId);
        role.setUpdateTime(new Date());
        roleMapper.insert(role);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult editRole(HandleRoleDto dto) {
        if (Objects.isNull(dto)){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        /**
         * 取出 userId
         */
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        Integer userId = (Integer) authentication.getPrincipal();

        Role role = new Role();
        role.setRoleId(dto.getRoleId());
        role.setColor(dto.getColor());
        role.setRemark(dto.getRemark());
        role.setRoleName(dto.getRoleName());
        role.setRoleKey(role.getRoleName());
        role.setUpdateBy(userId);
        role.setUpdateTime(new Date());
        roleMapper.updateById(role);
        return ResponseResult.okResult();
    }
}
