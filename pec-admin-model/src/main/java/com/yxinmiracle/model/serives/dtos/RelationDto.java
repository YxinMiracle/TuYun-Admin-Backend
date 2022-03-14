package com.yxinmiracle.model.serives.dtos;

import com.yxinmiracle.model.serives.pojos.Relation;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Data
public class RelationDto implements Serializable {

    private List<Relation> relationDataList;
    private Integer courseId;
    private String courseName;

    public boolean check() {
        if (StringUtils.isBlank(this.courseName)) {
            return false;
        }
        if (Objects.isNull(this.courseId)) {
            return false;
        }
        if (this.relationDataList.size() == 0) {
            return false;
        }
        return true;
    }

}
