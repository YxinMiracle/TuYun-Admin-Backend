package com.yxinmiracle.model.serives.pojos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("question_item")
public class QuestionItem implements Serializable {

    @TableId(value = "question_item_id",type = IdType.AUTO)
    private Integer questionItemId;

    @TableField(value = "question_item_content")
    private String questionItemContent;

    @TableField(value = "image")
    private String image;

    @TableField(value = "is_right")
    private short isRight;

    @TableField(value = "question_id")
    private Integer questionId;


}
