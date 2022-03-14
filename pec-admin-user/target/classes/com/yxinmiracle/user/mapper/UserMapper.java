package com.yxinmiracle.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yxinmiracle.model.user.dtos.PageUserDto;
import com.yxinmiracle.model.user.dtos.UserDto;
import com.yxinmiracle.model.user.pojos.User;
import com.yxinmiracle.model.user.vos.UserVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    List<UserVo> getUser(PageUserDto dto);

    int getUserCount(PageUserDto dto);
}
