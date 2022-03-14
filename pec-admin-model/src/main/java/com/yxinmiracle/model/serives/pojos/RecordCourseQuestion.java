package com.yxinmiracle.model.serives.pojos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.models.auth.In;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("record_course_question")
public class RecordCourseQuestion implements Serializable {
    @TableId(value = "record_course_question_id",type = IdType.AUTO)
    private Integer recordCourseQuestionId;

    @TableField(value = "course_id")
    private Integer courseId;

    @TableField(value = "course_name")
    private String courseName;

    @TableField(value = "course_question_count")
    private Integer courseQuestionCount; // 课程的总数

    @TableField(value = "single_choice_questions_count")
    private Integer singleChoiceQuestionsCount; // 单选题

    @TableField(value = "multiple_choice_questions_count")
    private Integer multipleChoiceQuestionsCount; // 多选题

    @TableField(value = "fill_the_blanks_questions_count")
    private Integer fillTheBlanksQuestionsCount; // 填空题

    @TableField(value = "answer_questions_count")
    private Integer answerQuestionsCount; // 简答题

}
