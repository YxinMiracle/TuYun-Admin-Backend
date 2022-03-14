package com.yxinmiracle.model.serives.dtos;

import com.yxinmiracle.model.common.dtos.PageRequestDto;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class CourseTagDto extends PageRequestDto implements Serializable {

    private String tagName;
    private Date createTagStartTime;
    private Date createTagEndTime;
    private Integer courseId;


}
