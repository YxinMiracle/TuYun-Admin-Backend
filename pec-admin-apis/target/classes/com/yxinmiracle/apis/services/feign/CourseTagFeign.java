package com.yxinmiracle.apis.services.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(name = "pec-admin-course",path = "/api/v1/tag")
public interface CourseTagFeign {
    @PostMapping("/tags")
    public List<String> getCourseTagByCourseTagIds(List<Integer> courseTagIds);

}
