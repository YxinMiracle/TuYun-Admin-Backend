package com.yxinmiracle.course.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yxinmiracle.model.serives.dtos.CourseTagDto;
import com.yxinmiracle.model.serives.pojos.CourseTag;
import com.yxinmiracle.model.serives.vos.CourseTagVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CourseTagMapper extends BaseMapper<CourseTag> {

    public List<CourseTagVo> showTagData(CourseTagDto dto);

    public Integer showTagDataCount(CourseTagDto dto);
}
