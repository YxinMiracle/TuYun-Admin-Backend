package com.yxinmiracle.course.service.impl;

import com.yxinmiracle.course.mapper.QuestionMapper;
import com.yxinmiracle.course.service.QuestionService;
import com.yxinmiracle.model.common.dtos.PageResponseResult;
import com.yxinmiracle.model.common.dtos.ResponseResult;
import com.yxinmiracle.model.common.enums.AppHttpCodeEnum;
import com.yxinmiracle.model.serives.dtos.QuestionDto;
import com.yxinmiracle.model.serives.vos.ChoiceQuestionVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionMapper questionMapper;

    @Override
    public ResponseResult getQuestionData(QuestionDto dto) {
        if (Objects.isNull(dto)){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        System.out.println(dto);
        Integer courseId = dto.getCourseId();
        if (Objects.isNull(courseId)){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        List<ChoiceQuestionVo> choiceQuestionList = questionMapper.getChoiceList(dto);
        Integer total = questionMapper.getChoiceListCount(dto);
        ResponseResult responseResult = new PageResponseResult(dto.getPage(),dto.getSize(),total);
        responseResult.setData(choiceQuestionList);
        responseResult.setCode(AppHttpCodeEnum.SUCCESS.getCode());
        responseResult.setErrorMessage(AppHttpCodeEnum.SUCCESS.getErrorMessage());
        return responseResult;
//        Integer searchType = dto.getSearchType();
//        if (Objects.isNull(dto.getSearchType())){
//            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
//        }
        // searchType 为搜索题目的类型 1表示多选和单选 2表示解答和填空
//        if (searchType == 1){
//
//        }
//        if (searchType == 2){
//
//        }
//        return null;
    }
}
