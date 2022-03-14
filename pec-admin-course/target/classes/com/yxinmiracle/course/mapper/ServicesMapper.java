package com.yxinmiracle.course.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yxinmiracle.model.serives.dtos.CourseDto;
import com.yxinmiracle.model.serives.pojos.Services;
import com.yxinmiracle.model.serives.vos.CourseVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ServicesMapper extends BaseMapper<Services> {



}
