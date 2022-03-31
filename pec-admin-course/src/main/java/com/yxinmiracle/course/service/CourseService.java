package com.yxinmiracle.course.service;

import com.yxinmiracle.model.common.dtos.ResponseResult;
import com.yxinmiracle.model.serives.dtos.CourseDto;
import com.yxinmiracle.model.serives.dtos.HandleCourseDto;
import com.yxinmiracle.model.serives.pojos.Course;

import java.util.List;

public interface CourseService {
    ResponseResult getCourse(CourseDto dto);

    ResponseResult addCourse(HandleCourseDto dto);

    ResponseResult updateCourse(HandleCourseDto dto);

    ResponseResult deleteCourse(Integer courseId);

    ResponseResult<List<Course>> getAllCourse();

    ResponseResult getCourseByCourseId(Integer courseId);
}
