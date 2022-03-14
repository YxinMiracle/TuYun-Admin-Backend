package com.yxinmiracle.model.serives.vos;

import lombok.Data;

import java.io.Serializable;

@Data
public class ShowRelationCourseVo implements Serializable {

    private String courseName;
    private Integer relationCount;
    private Integer courseId;


}
