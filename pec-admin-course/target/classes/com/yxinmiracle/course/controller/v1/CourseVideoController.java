package com.yxinmiracle.course.controller.v1;

import com.yxinmiracle.apis.services.CourseVideoControllerApi;
import com.yxinmiracle.course.service.CourseVideoService;
import com.yxinmiracle.model.common.dtos.ResponseResult;
import com.yxinmiracle.model.serives.dtos.CourseVideoCountDto;
import com.yxinmiracle.model.serives.dtos.CourseVideoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/course/video")
public class CourseVideoController implements CourseVideoControllerApi {

    @Autowired
    private CourseVideoService courseVideoService;

    @Override
    @PostMapping
    public ResponseResult getCourseVideoCountData(@RequestBody CourseVideoCountDto dto) {
        return courseVideoService.getCourseVideoCountData(dto);
    }

    @RequestMapping(value = "/handel/{courseId}",method = RequestMethod.PUT)
    @Override
    public ResponseResult updateCourseVideoCount(@PathVariable("courseId") Integer courseId) {
        return courseVideoService.updateCourseVideoCount(courseId);
    }


}
