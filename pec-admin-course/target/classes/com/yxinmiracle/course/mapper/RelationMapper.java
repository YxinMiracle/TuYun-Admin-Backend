package com.yxinmiracle.course.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yxinmiracle.model.serives.dtos.ShowRelationDto;
import com.yxinmiracle.model.serives.pojos.Relation;
import com.yxinmiracle.model.serives.vos.ShowRelationCourseVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RelationMapper extends BaseMapper<Relation> {

    List<ShowRelationCourseVo> getShowRelationData(ShowRelationDto dto);

    Integer getShowRelationDataCount(ShowRelationDto dto);

}
