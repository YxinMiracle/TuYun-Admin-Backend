package com.yxinmiracle.model.serives.dtos;

import com.yxinmiracle.model.serives.pojos.CourseTag;
import com.yxinmiracle.model.serives.pojos.Relation;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public class CourseTagAndTagPropertyDto implements Serializable {
    private CourseTag newTagData;
    private Map<String, String> retPropertyMap;
}
