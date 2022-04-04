package com.yxinmiracle.apis.services;

import com.yxinmiracle.model.common.dtos.ResponseResult;
import com.yxinmiracle.model.serives.dtos.CourseVideoCountDto;
import com.yxinmiracle.model.serives.dtos.CourseVideoDto;

public interface CourseVideoControllerApi {

    public ResponseResult getCourseVideoCountData(CourseVideoCountDto dto);

    public ResponseResult updateCourseVideoCount(Integer courseId);

}
