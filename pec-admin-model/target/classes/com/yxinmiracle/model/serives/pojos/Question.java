package com.yxinmiracle.model.serives.pojos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("question")
public class Question implements Serializable {

    @TableId(value = "question_id",type = IdType.AUTO)
    private Integer questionId;

    @TableField(value = "subject")
    private String subject;

    @TableField(value = "type")
    private short type;

    @TableField(value = "difficulty")
    private short difficulty;

    @TableField(value = "analysis")
    private String analysis;

    @TableField(value = "analysis_video")
    private String analysisVideo;

    @TableField(value = "remark")
    private String remark;

    @TableField(value = "is_frequency")
    private short isFrequency; // 是否是高频考点

    @TableField(value = "is_classic")
    private short isClassic; // 精选题目

    @TableField(value = "question_create_time")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date questionCreateTime;

    @TableField(value = "question_update_time")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date questionUpdateTime;

    @TableField(value = "create_question_user_id")
    private Integer createQuestionUserId;

    @TableField(value = "create_question_user_name")
    private String createQuestionUserName;

    @TableField(value = "course_id")
    private Integer courseId;

    @TableField(value = "course_name")
    private String courseName;

}
