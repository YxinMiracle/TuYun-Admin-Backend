package com.yxinmiracle.model.serives.dtos;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.parameters.P;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Data
public class HandleCourseDto {

    private Integer courseId;
    private String courseName;
    private Integer courseCategoryItemId;
    private Double price;
    private Integer servicesId;
    private List<Integer> teacherIdList;
    private short isQuality;
    private short isShow;
    private Date startTime;
    private Date endTime;
    private String image;
    private short infoType;
    private String courseArticle;
    private List<String> imageList;

    public boolean check(){
        if (StringUtils.isBlank(this.courseName) || Objects.isNull(this.courseName) || this.courseName.length() > 60){
            return false;
        }
        if (Objects.isNull(this.courseCategoryItemId)){
            return false;
        }
        if (Objects.isNull(this.price)){
            return false;
        }
        if (Objects.isNull(this.servicesId)){
            return false;
        }
        if (Objects.isNull(this.teacherIdList)){
            return false;
        }
        if (Objects.isNull(this.isQuality)){
            return false;
        }
        if (Objects.isNull(this.isShow)) {
            return false;
        }
        if (Objects.isNull(this.startTime)){
            return false;
        }
        if (Objects.isNull(this.endTime)){
            return false;
        }
        if (Objects.isNull(this.image)){
            return false;
        }
        if (Objects.isNull(this.infoType)){
            return false;
        }
        return true;
    }

}
