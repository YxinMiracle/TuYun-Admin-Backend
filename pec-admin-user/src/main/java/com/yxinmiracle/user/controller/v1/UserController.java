package com.yxinmiracle.user.controller.v1;

import com.yxinmiracle.apis.user.UserControllerApi;
import com.yxinmiracle.model.common.dtos.ResponseResult;
import com.yxinmiracle.model.user.dtos.PageUserDto;
import com.yxinmiracle.model.user.dtos.UserDto;
import com.yxinmiracle.model.user.pojos.User;
import com.yxinmiracle.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserController implements UserControllerApi {

    @Autowired
    private UserService userService;

    @RequestMapping("/login")
    public ResponseResult login(@RequestBody User user){
        System.out.println(user);
        return userService.login(user);
    }

    @PreAuthorize("hasAuthority('system:user:query')")
    @PostMapping
    @Override
    public ResponseResult getUser(@RequestBody PageUserDto dto){
        return userService.getUser(dto);
    }

    @RequestMapping(value = "/handle", method = RequestMethod.POST) // 添加用户
    @Override
    public ResponseResult addUser(@RequestBody UserDto dto){
        return userService.addUser(dto);
    }

    @RequestMapping(value = "/handle", method = RequestMethod.PUT) // 更新用户
    @Override
    public ResponseResult updateUser(@RequestBody UserDto dto) {
        return userService.updateUser(dto);
    }

    @RequestMapping(value = "/handle/{userId}", method = RequestMethod.DELETE)
    @Override
    public ResponseResult deleteUser(@PathVariable("userId") String userId) {
        return userService.deleteUser(userId);
    }


    @Override
    @GetMapping("/servicesId/{servicesId}")
    public ResponseResult getUserByServicesId(@PathVariable("servicesId") Integer servicesId) {
        return userService.getUserByServicesId(servicesId);
    }

    @RequestMapping("/test")
    @PreAuthorize("hasAuthority('system:teacher:add')")
    public String test(){
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        Integer userId = (Integer) authentication.getPrincipal();
        System.out.println(userId);
        return "test";
    }

}
