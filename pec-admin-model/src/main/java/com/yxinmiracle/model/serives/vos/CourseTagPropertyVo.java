package com.yxinmiracle.model.serives.vos;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public class CourseTagPropertyVo implements Serializable {

    private Integer courseTagId;
    private String tagDesc;
    private String courseName;
    private Map<String,String> propertyMap;

}
