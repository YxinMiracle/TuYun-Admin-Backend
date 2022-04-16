package com.yxinmiracle.model.search.es;

import com.yxinmiracle.model.serives.pojos.Question;
import com.yxinmiracle.model.serives.pojos.QuestionItem;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class EsQuestion implements Serializable {
    private Question question; // 用来存放问题的主体
    private List<QuestionItem> questionItemList; // 用来存放问题的选项
    private List<Integer> questionTagIds;
    private List<String> questionTagList; // 用来存放问题所包含的知识点
}
