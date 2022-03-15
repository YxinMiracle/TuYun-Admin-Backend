package com.yxinmiracle.model.serives.dtos;

import com.yxinmiracle.model.serives.pojos.QuestionItem;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Data
public class HandleQuestionDto implements Serializable {

    private Integer questionId;
    private String subject;
    private short type;
    private short difficulty;
    private short isFrequency;
    private short isClassic;
    private String analysis;
    private String analysisVideo;
    private String remark;
    private Integer courseId;
    private List<Integer> courseTagIdList;
    private List<QuestionItem> questionItemList;

    public boolean check(){
        if (StringUtils.isBlank(this.subject)){
            return false;
        }
        if (StringUtils.isBlank(this.analysis)){
            return false;
        }
        if (Objects.isNull(this.type) || Objects.isNull(this.difficulty) || Objects.isNull(this.isFrequency) || Objects.isNull(this.courseId) || Objects.isNull(this.isClassic)){
            return false;
        }
        return true;
    }


}
