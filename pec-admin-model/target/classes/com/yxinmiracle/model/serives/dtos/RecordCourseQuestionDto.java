package com.yxinmiracle.model.serives.dtos;

import com.yxinmiracle.model.common.dtos.PageRequestDto;
import lombok.Data;

import java.io.Serializable;

@Data
public class RecordCourseQuestionDto extends PageRequestDto implements Serializable {
    private String courseName;
}
