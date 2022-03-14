package com.yxinmiracle.model.serives.dtos;

import com.yxinmiracle.model.common.dtos.PageRequestDto;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class QuestionDto extends PageRequestDto implements Serializable {

    private String subject;
    private short type;
    private short difficulty;
    private short isFrequency;
    private short isClassic;
    private Date createQuestionTimeStart;
    private Date createQuestionTimeEnd;
    private List<Integer> courseTagIdList;
    private Integer courseId;

}
