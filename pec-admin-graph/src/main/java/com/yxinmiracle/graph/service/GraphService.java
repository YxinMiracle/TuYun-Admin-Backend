package com.yxinmiracle.graph.service;

import com.yxinmiracle.model.common.dtos.ResponseResult;

public interface GraphService {
    ResponseResult getConfig();

    ResponseResult getCypherByCourseName(String courseName);
}
