package com.yxinmiracle.apis.services;

import com.yxinmiracle.model.common.dtos.ResponseResult;
import com.yxinmiracle.model.serives.dtos.HandleQuestionDto;
import com.yxinmiracle.model.serives.dtos.QuestionDto;
import com.yxinmiracle.model.serives.vos.QuestionEchartsVo;

import java.util.List;

public interface QuestionControllerApi {
    public ResponseResult getQuestionData(QuestionDto dto);

    /**
     * get different type question count by courseId
     * send ResponseResult<List<QuestionEchartsVo>> Object to show picture
     * @param courseId Integer
     * @return ResponseResult<List<QuestionEchartsVo>>
     */
    public ResponseResult<List<QuestionEchartsVo>> getQuestionEchartsData(Integer courseId);

    /**
     * insert into mysql question,question_item,question_tag table
     * add question api
     * @param dto HandleQuestionDto
     * @return ResponseResult
     */
    public ResponseResult addQuestion(HandleQuestionDto dto);

    /**
     * update question
     * @param dto
     * @return
     */
    public ResponseResult updateQuestion(HandleQuestionDto dto);

    /**
     * delete question
     * @param questionId questionId
     * @return ResponseResult
     */
    public ResponseResult deleteQuestion(Integer questionId);
}
