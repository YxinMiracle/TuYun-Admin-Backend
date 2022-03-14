package com.yxinmiracle.apis.services;

import com.yxinmiracle.model.common.dtos.ResponseResult;
import com.yxinmiracle.model.serives.dtos.RecordCourseQuestionDto;
import com.yxinmiracle.model.serives.pojos.RecordCourseQuestion;

public interface RecordCourseQuestionControllerApi {

    /**
     * 接受展示课程问题页面的数据
     * 需要展示的数据有：
     * 课程名称 已有的问题数量
     * @param dto
     * @return
     */
    public ResponseResult showRecordCourseQuestion(RecordCourseQuestionDto dto);

}
