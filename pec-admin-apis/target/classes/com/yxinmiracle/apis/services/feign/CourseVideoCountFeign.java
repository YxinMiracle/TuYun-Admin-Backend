package com.yxinmiracle.apis.services.feign;


import com.yxinmiracle.model.common.dtos.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "pec-admin-course",path = "/api/v1/course/video")
public interface CourseVideoCountFeign {
    @RequestMapping(value = "/handel/{courseId}",method = RequestMethod.PUT)
    public ResponseResult updateCourseVideoCount(@PathVariable("courseId") Integer courseId);

}
