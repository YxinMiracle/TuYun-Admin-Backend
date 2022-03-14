package com.yxinmiracle.course.service;

import com.yxinmiracle.model.common.dtos.ResponseResult;
import com.yxinmiracle.model.serives.dtos.QuestionDto;

public interface QuestionService {
    ResponseResult getQuestionData(QuestionDto dto);
}
