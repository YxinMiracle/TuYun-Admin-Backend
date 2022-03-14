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
@TableName("relation")
public class Relation implements Serializable {

    @TableId(value = "relation_id",type = IdType.AUTO)
    private Integer relationId;

    @TableField(value = "knowledge_point_start_name")
    private String knowledgePointStartName;

    @TableField(value = "relation_name")
    private String relationName;

    @TableField(value = "knowledge_point_end_name")
    private String knowledgePointEndName;

    @TableField(value = "relation_create_time")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date relationCreateTime;

    @TableField(value = "relation_update_time")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date relationUpdateTime;

    @TableField(value = "course_id")
    private Integer courseId;

    @TableField(value = "course_name")
    private String courseName;

    @TableField(value = "relation_is_show")
    private short relationIsShow;

    @TableField(value = "relation_is_delete")
    private short relationIsDelete;
}
