package com.yxinmiracle.apis.services.feign.api;

import java.util.List;

public interface CourseControllerFeignApi {
    /**
     *
     * @param courseTagIds
     * @return
     */
    public List<String> getCourseTagByCourseTagIds(List<Integer> courseTagIds);
}
