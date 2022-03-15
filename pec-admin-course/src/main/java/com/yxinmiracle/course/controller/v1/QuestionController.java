package com.yxinmiracle.course.controller.v1;

import com.yxinmiracle.apis.services.QuestionControllerApi;
import com.yxinmiracle.course.service.QuestionService;
import com.yxinmiracle.model.common.dtos.ResponseResult;
import com.yxinmiracle.model.serives.dtos.HandleQuestionDto;
import com.yxinmiracle.model.serives.dtos.QuestionDto;
import com.yxinmiracle.model.serives.vos.QuestionEchartsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @Override
    @GetMapping("/{courseId}")
    public ResponseResult<List<QuestionEchartsVo>> getQuestionEchartsData(@PathVariable("courseId") Integer courseId) {
        return questionService.getQuestionEchartsData(courseId);
    }

    @Override
    @RequestMapping(value = "/handle",method = RequestMethod.POST)
    public ResponseResult addQuestion(@RequestBody HandleQuestionDto dto) {
        return questionService.addQuestion(dto);
    }

    @Override
    @RequestMapping(value = "/handle",method = RequestMethod.PUT)
    public ResponseResult updateQuestion(@RequestBody HandleQuestionDto dto) {
        return questionService.updateQuestion(dto);
    }

    @RequestMapping(value = "/handle/{questionId}",method = RequestMethod.DELETE)
    @Override
    public ResponseResult deleteQuestion(@PathVariable("questionId") Integer questionId) {
        return questionService.deleteQuestion(questionId);
    }
}
