package com.yxinmiracle.model.serives.pojos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class KnowledgePoint implements Serializable {

    @TableId(value = "knowledge_point_id",type = IdType.AUTO)
    private Integer knowledgePointId;

    @TableField(value = "knowledge_point_name")
    private String knowledgePointName;

    @TableField(value = "course_id")
    private Integer courseId;

    @TableField(value = "course_name")
    private String courseName;

    @TableField(value = "knowledge_point_create_time")
    private Date knowledgePointCreateTime;

    @TableField(value = "knowledge_point_update_time")
    private Date knowledgePointUpdateTime;

}
