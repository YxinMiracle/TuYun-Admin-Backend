package com.yxinmiracle.course.service;

import com.yxinmiracle.model.common.dtos.ResponseResult;
import com.yxinmiracle.model.serives.dtos.HandleQuestionDto;
import com.yxinmiracle.model.serives.dtos.QuestionDto;
import com.yxinmiracle.model.serives.vos.QuestionEchartsVo;

import java.util.List;

public interface QuestionService {
    ResponseResult getQuestionData(QuestionDto dto);

    ResponseResult<List<QuestionEchartsVo>> getQuestionEchartsData(Integer courseId);

    ResponseResult addQuestion(HandleQuestionDto dto);

    ResponseResult updateQuestion(HandleQuestionDto dto);

    ResponseResult deleteQuestion(Integer questionId);
}
