package com.yxinmiracle.model.serives.vos;

import com.yxinmiracle.model.serives.pojos.CourseTag;
import com.yxinmiracle.model.user.pojos.User;
import lombok.AllArgsConstructor;
import lombok.Data;


import java.io.Serializable;
import java.util.Date;

@Data
public class CourseTagVo extends CourseTag implements Serializable {

    private User createdCourseTagUser;
    private Integer haveRelationCount;

}
