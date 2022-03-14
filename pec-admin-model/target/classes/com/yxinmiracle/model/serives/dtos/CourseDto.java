package com.yxinmiracle.model.serives.dtos;

import com.yxinmiracle.model.common.dtos.PageRequestDto;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class CourseDto extends PageRequestDto implements Serializable {

    private String courseName;
    private Date openClassStartTime;
    private Date openClassEndTime;

    private Date finishClassStartTime;
    private Date finisClassEndTime;

    private Date createClassStartTime;
    private Date createClassEndTime;

    private Integer courseCategoryItemId;

    private Integer startPrice;
    private Integer endPrice;

    private short isQuality;

    private char letter;

    private Integer servicesId;


}
