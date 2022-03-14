package com.yxinmiracle.apis.services;

import com.yxinmiracle.model.common.dtos.ResponseResult;
import com.yxinmiracle.model.serives.dtos.QuestionDto;

public interface QuestionControllerApi {
    public ResponseResult getQuestionData(QuestionDto dto);
}
