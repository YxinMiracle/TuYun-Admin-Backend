package com.yxinmiracle.course.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yxinmiracle.model.user.dtos.PageUserDto;
import com.yxinmiracle.model.user.pojos.User;
import com.yxinmiracle.model.user.vos.UserVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {

}
