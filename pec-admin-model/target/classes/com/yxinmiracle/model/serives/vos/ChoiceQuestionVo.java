package com.yxinmiracle.model.serives.vos;

import com.yxinmiracle.model.serives.pojos.CourseTag;
import com.yxinmiracle.model.serives.pojos.Question;
import com.yxinmiracle.model.serives.pojos.QuestionItem;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ChoiceQuestionVo extends Question implements Serializable {
    private List<CourseTag> courseTagList;
    private List<QuestionItem> questionItemList;
}
