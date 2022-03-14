package com.yxinmiracle.course.service;

import com.yxinmiracle.model.common.dtos.ResponseResult;
import com.yxinmiracle.model.serives.dtos.RecordCourseQuestionDto;

public interface RecordCourseQuestionService {
    ResponseResult showRecordCourseQuestion(RecordCourseQuestionDto dto);
}
