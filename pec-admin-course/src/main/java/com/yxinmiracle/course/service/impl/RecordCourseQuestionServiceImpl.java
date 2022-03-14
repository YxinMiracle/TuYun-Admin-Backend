package com.yxinmiracle.course.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yxinmiracle.course.mapper.RecordCourseQuestionMapper;
import com.yxinmiracle.course.service.RecordCourseQuestionService;
import com.yxinmiracle.model.common.dtos.PageResponseResult;
import com.yxinmiracle.model.common.dtos.ResponseResult;
import com.yxinmiracle.model.common.enums.AppHttpCodeEnum;
import com.yxinmiracle.model.serives.dtos.RecordCourseQuestionDto;
import com.yxinmiracle.model.serives.pojos.RecordCourseQuestion;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class RecordCourseQuestionServiceImpl implements RecordCourseQuestionService {
    @Autowired
    private RecordCourseQuestionMapper recordCourseQuestionMapper;

    @Override
    public ResponseResult showRecordCourseQuestion(RecordCourseQuestionDto dto) {
        if (Objects.isNull(dto)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        IPage<RecordCourseQuestion> page = new Page<>(dto.getPage(),dto.getSize());
        LambdaQueryWrapper<RecordCourseQuestion> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(dto.getCourseName())){
            lambdaQueryWrapper.like(RecordCourseQuestion::getCourseName,dto.getCourseName());
        }
        IPage<RecordCourseQuestion> recordCourseQuestionIPage = recordCourseQuestionMapper.selectPage(page, lambdaQueryWrapper);
        ResponseResult responseResult = new PageResponseResult(dto.getPage(),dto.getSize(),(int)recordCourseQuestionIPage.getTotal());
        responseResult.setCode(AppHttpCodeEnum.SUCCESS.getCode());
        responseResult.setData(recordCourseQuestionIPage.getRecords());
        responseResult.setErrorMessage(AppHttpCodeEnum.SUCCESS.getErrorMessage());
        return responseResult;
    }
}
