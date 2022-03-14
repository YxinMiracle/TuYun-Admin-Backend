package com.yxinmiracle.course.controller.v1;

import com.yxinmiracle.apis.services.RecordCourseQuestionControllerApi;
import com.yxinmiracle.course.service.RecordCourseQuestionService;
import com.yxinmiracle.model.common.dtos.ResponseResult;
import com.yxinmiracle.model.serives.dtos.RecordCourseQuestionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/record/course/question")
public class RecordCourseQuestionController implements RecordCourseQuestionControllerApi {

    @Autowired
    private RecordCourseQuestionService recordCourseQuestionService;

    @PostMapping
    @Override
    public ResponseResult showRecordCourseQuestion(@RequestBody RecordCourseQuestionDto dto) {
        return recordCourseQuestionService.showRecordCourseQuestion(dto);
    }
}
