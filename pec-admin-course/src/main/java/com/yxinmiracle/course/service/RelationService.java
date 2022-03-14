package com.yxinmiracle.course.service;

import com.yxinmiracle.model.common.dtos.ResponseResult;
import com.yxinmiracle.model.serives.dtos.RelationDto;
import com.yxinmiracle.model.serives.dtos.ShowRelationDto;

public interface RelationService {
    ResponseResult addRelation(RelationDto dto);

    ResponseResult showRelationCourseData(ShowRelationDto dto);

    ResponseResult getTagRelationDataByTagNameAndCourseId(String tagName, Integer courseId);

    ResponseResult updateRelation(RelationDto dto);
}
