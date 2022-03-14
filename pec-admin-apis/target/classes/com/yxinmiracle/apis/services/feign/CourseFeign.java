package com.yxinmiracle.apis.services.feign;

import com.yxinmiracle.model.common.dtos.ResponseResult;
import com.yxinmiracle.model.serives.pojos.Course;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "pec-admin-course",path = "/api/v1/course")
public interface CourseFeign {

    @GetMapping("/all")
    public ResponseResult<List<Course>> getAllCourse();

}
