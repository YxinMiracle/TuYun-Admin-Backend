package com.yxinmiracle.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yxinmiracle.model.user.pojos.Menu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MenuMapper extends BaseMapper<Menu> {

    List<Menu> getMenuListByRoleId(Integer roleId);

}
