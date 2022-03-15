package com.yxinmiracle.course.service;

import com.yxinmiracle.model.common.dtos.ResponseResult;
import com.yxinmiracle.model.serives.dtos.CourseTagAndTagPropertyDto;
import com.yxinmiracle.model.serives.dtos.CourseTagDto;
import com.yxinmiracle.model.serives.vos.CourseTagPropertyVo;

import java.util.Set;

public interface CourseTagService {
    ResponseResult showTagData(CourseTagDto dto);

    ResponseResult updateCourseTagNameAndProperty(CourseTagAndTagPropertyDto dto);

    ResponseResult<CourseTagPropertyVo> getCourseTagProperty(Integer tagId);

    ResponseResult deleteTag(Integer tagId);

    ResponseResult resumeTag(Integer tagId);

    ResponseResult getAllCourseTag(Integer courseId);
}
