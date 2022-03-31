package com.yxinmiracle.apis.services;

import com.yxinmiracle.model.common.dtos.ResponseResult;
import com.yxinmiracle.model.serives.dtos.CourseDto;
import com.yxinmiracle.model.serives.dtos.HandleCourseDto;

public interface CourseControllerApi {
    public ResponseResult getCourse(CourseDto dto);

    public ResponseResult addCourse(HandleCourseDto dto);

    public ResponseResult updateCourse(HandleCourseDto dto);

    public ResponseResult deleteCourse(Integer courseId);

    public ResponseResult getAllCourse();

    public ResponseResult getCourseByCourseId(Integer courseId);
}
