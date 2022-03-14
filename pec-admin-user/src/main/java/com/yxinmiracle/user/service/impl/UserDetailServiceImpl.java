package com.yxinmiracle.user.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yxinmiracle.model.user.pojos.*;
import com.yxinmiracle.model.login.pojos.LoginUser;
import com.yxinmiracle.user.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private MenuMapper menuMapper;

    @Override
    public UserDetails loadUserByUsername(String userAccount) throws UsernameNotFoundException {
        User loginUser = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUserAccount, userAccount));
        System.out.println(loginUser);
        if (Objects.isNull(loginUser)){
            throw new RuntimeException("用户名密码错误");
        }
        Integer userId = loginUser.getUserId();
        List<UserRole> userRoles = userRoleMapper.selectList(Wrappers.<UserRole>lambdaQuery().eq(UserRole::getUserId, userId));
        Set<Integer> menuIdSet = new HashSet();
        for (UserRole userRole : userRoles) {
            Integer roleId = userRole.getRoleId();
            List<RoleMenu> roleMenus = roleMenuMapper.selectList(Wrappers.<RoleMenu>lambdaQuery().eq(RoleMenu::getRoleId, roleId));
            for (RoleMenu roleMenu : roleMenus) {
                menuIdSet.add(roleMenu.getMenuId());
            }
        }
        List<String> list = new ArrayList<>();
        for (Integer menuId : menuIdSet) {
            Menu menu = menuMapper.selectOne(Wrappers.<Menu>lambdaQuery().eq(Menu::getMenuId, menuId));
            String perms = menu.getPerms();
            list.add(perms);
        }
//        List<String> list = new ArrayList<>(Arrays.asList("test","admin"));
        return new LoginUser(loginUser,list);
    }
}
