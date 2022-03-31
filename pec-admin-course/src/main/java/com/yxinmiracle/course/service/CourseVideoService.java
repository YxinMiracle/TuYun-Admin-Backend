package com.yxinmiracle.course.service;

import com.yxinmiracle.model.common.dtos.ResponseResult;
import com.yxinmiracle.model.serives.dtos.CourseVideoCountDto;
import com.yxinmiracle.model.serives.dtos.CourseVideoDto;

public interface CourseVideoService {
    ResponseResult getCourseVideoCountData(CourseVideoCountDto dto);

    ResponseResult updateCourseVideoCount(Integer courseId);
}
