package com.yxinmiracle.course.controller.v1;

import com.yxinmiracle.apis.services.QuestionControllerApi;
import com.yxinmiracle.course.service.QuestionService;
import com.yxinmiracle.model.common.dtos.ResponseResult;
import com.yxinmiracle.model.serives.dtos.QuestionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/question")
public class QuestionController implements QuestionControllerApi {
    @Autowired
    private QuestionService questionService;

    @Override
    @PostMapping
    public ResponseResult getQuestionData(@RequestBody QuestionDto dto) {
        return questionService.getQuestionData(dto);
    }
}
