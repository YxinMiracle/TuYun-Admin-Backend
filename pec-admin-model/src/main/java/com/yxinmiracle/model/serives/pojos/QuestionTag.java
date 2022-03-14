package com.yxinmiracle.model.serives.pojos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("question_tag")
public class QuestionTag implements Serializable {

    @TableId(value = "question_tag_id",type = IdType.AUTO)
    private Integer questionTagId;

    @TableField(value = "question_id")
    private Integer questionId;

    @TableField(value = "course_tag_id")
    private Integer courseTagId;

}
