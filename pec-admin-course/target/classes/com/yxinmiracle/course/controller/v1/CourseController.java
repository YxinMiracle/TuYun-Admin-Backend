package com.yxinmiracle.course.controller.v1;

import com.yxinmiracle.apis.services.CourseControllerApi;
import com.yxinmiracle.course.service.CourseService;
import com.yxinmiracle.model.common.dtos.ResponseResult;
import com.yxinmiracle.model.serives.dtos.CourseDto;
import com.yxinmiracle.model.serives.dtos.HandleCourseDto;
import com.yxinmiracle.model.serives.pojos.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/course")
@RestController
public class CourseController implements CourseControllerApi {
    @Autowired
    private CourseService courseService;

    @PostMapping
    @Override
    public ResponseResult getCourse(@RequestBody CourseDto dto) {
        return courseService.getCourse(dto);
    }

    @Override
    @RequestMapping(value = "handle", method = RequestMethod.POST)
    public ResponseResult addCourse(@RequestBody HandleCourseDto dto) {
        return courseService.addCourse(dto);
    }

    @Override
    @RequestMapping(value = "handle", method = RequestMethod.PUT)
    public ResponseResult updateCourse(@RequestBody HandleCourseDto dto) {
        return courseService.updateCourse(dto);
    }

    @Override
    @RequestMapping(value = "/handle/{courseId}",method = RequestMethod.DELETE)
    public ResponseResult deleteCourse(@PathVariable("courseId") Integer courseId) {
        return courseService.deleteCourse(courseId);
    }

    @Override
    @GetMapping("/all")
    public ResponseResult<List<Course>> getAllCourse() {
        return courseService.getAllCourse();
    }

    @Override
    @GetMapping("/{courseId}")
    public ResponseResult getCourseByCourseId(@PathVariable("courseId") Integer courseId) {
        return courseService.getCourseByCourseId(courseId);
    }
}
