package com.yxinmiracle.model.serives.vos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;


/**
 * 存储问题分类统计数据，传递给前端
 */
@Data
@AllArgsConstructor
public class QuestionEchartsVo implements Serializable {

    private Integer value;
    private String name;

}
