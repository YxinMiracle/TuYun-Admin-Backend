package com.yxinmiracle.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yxinmiracle.model.common.dtos.PageResponseResult;
import com.yxinmiracle.model.common.dtos.ResponseResult;
import com.yxinmiracle.model.common.enums.AppHttpCodeEnum;
import com.yxinmiracle.model.user.dtos.PageUserDto;
import com.yxinmiracle.model.user.dtos.UserDto;
import com.yxinmiracle.model.user.pojos.Role;
import com.yxinmiracle.model.user.pojos.User;
import com.yxinmiracle.model.user.pojos.UserRole;
import com.yxinmiracle.model.user.vos.LoginUserVo;
import com.yxinmiracle.model.user.vos.UserVo;
import com.yxinmiracle.model.login.pojos.LoginUser;
import com.yxinmiracle.user.mapper.RoleMapper;
import com.yxinmiracle.user.mapper.UserMapper;
import com.yxinmiracle.user.mapper.UserRoleMapper;
import com.yxinmiracle.user.service.UserService;

import com.yxinmiracle.utils.common.BCrypt;
import com.yxinmiracle.utils.common.ImageUrlUtil;
import com.yxinmiracle.utils.common.JsoupUtil;
import com.yxinmiracle.utils.common.JwtUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.NestedServletException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {
    Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${host}")
    private String HOST;


    @Override
    public ResponseResult login(User user) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserAccount(), user.getPassword());
            Authentication authenticate = authenticationManager.authenticate(authenticationToken);// 他会去调用userDetail方法
            if (Objects.isNull(authenticate)) {
                throw new RuntimeException("登陆失败");
            }
            LoginUser loginUser = (LoginUser) authenticate.getPrincipal();

            stringRedisTemplate.boundValueOps("pec.user_" + loginUser.getUser().getUserId()).set(JSON.toJSONString(loginUser));
            LoginUserVo userVo = new LoginUserVo();
            String token = JwtUtil.createToken(loginUser.getUser().getUserId());
//        String token = AppJwtUtil.getToken(loginUser.getUser().getUserId());
            BeanUtils.copyProperties(loginUser.getUser(), userVo);
            userVo.setToken(token);
            return new ResponseResult().ok(userVo);
        } catch (Exception e) {
            logger.error("登陆jwt获取出现错误,{}", e.getMessage());
            throw new RuntimeException("登陆jwt获取出现错误");
        }
    }

    @Override
    public ResponseResult getUser(PageUserDto dto) {
        if (Objects.isNull(dto)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        dto.checkParam();
        List<UserVo> users = userMapper.getUser(dto);
        int total = userMapper.getUserCount(dto);
        ResponseResult<List<UserVo>> responseResult = new PageResponseResult(dto.getPage(), dto.getSize(), total);
        responseResult.setHost(HOST);
        responseResult.setCode(AppHttpCodeEnum.SUCCESS.getCode());
        responseResult.setData(users);
        return responseResult;
    }

    @Override
    @Transactional
    public ResponseResult addUser(UserDto dto) {
        if (Objects.isNull(dto)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        if (!dto.check()) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        // 取手机号后六位作为默认密码
        User insertUser = new User();
        BeanUtils.copyProperties(dto, insertUser);
        String encodePassword = passwordEncoder.encode(dto.getPhone().substring(dto.getPhone().length() - 6));
        insertUser.setPassword(encodePassword); // todo
        insertUser.setImage(ImageUrlUtil.repalceImageUrl(insertUser.getImage(), HOST));
        insertUser.setCreateTime(new Date());
        insertUser.setUpdateTime(new Date());
        insertUser.setUserArticle(JsoupUtil.clean(dto.getUserArticle()));
        userMapper.insert(insertUser);
        for (int i = 0; i < dto.getRoleIdList().size(); i++) {
            UserRole userRole = new UserRole(insertUser.getUserId(), dto.getRoleIdList().get(i));
            userRoleMapper.insert(userRole);
        }
        logger.info("用户添加成功,{}", insertUser);
        System.out.println(dto);
        return ResponseResult.okResult();
    }

    @Override
    @Transactional
    public ResponseResult updateUser(UserDto dto) {
        if (Objects.isNull(dto)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        if (!dto.check()) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        User insertUser = new User();
        BeanUtils.copyProperties(dto, insertUser);
        insertUser.setImage(ImageUrlUtil.repalceImageUrl(insertUser.getImage(), HOST));
        insertUser.setUpdateTime(new Date());
        insertUser.setUserArticle(JsoupUtil.clean(dto.getUserArticle()));
        userMapper.updateById(insertUser);
        /*
        * 先将用户-角色表中 user_id = 该用户的id 的 数据删除 然后再进行添加
        * */
        userRoleMapper.delete(Wrappers.<UserRole>lambdaQuery().eq(UserRole::getUserId,dto.getUserId()));
        for (int i = 0; i < dto.getRoleIdList().size(); i++) {
            UserRole userRole = new UserRole(insertUser.getUserId(), dto.getRoleIdList().get(i));
            userRoleMapper.insert(userRole);
        }
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteUser(String userId) {
        if (userId == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        LambdaUpdateWrapper<User> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(User::getUserId,userId).set(User::getUserIsDelete,User.isDeleteEnum.isDelete.getCode());
        userMapper.update(null,lambdaUpdateWrapper);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getUserByServicesId(Integer servicesId) {
        List<User> users = userMapper.selectList(Wrappers.<User>lambdaQuery().eq(User::getServicesId, servicesId));
        return ResponseResult.okResult(users);
    }
}
