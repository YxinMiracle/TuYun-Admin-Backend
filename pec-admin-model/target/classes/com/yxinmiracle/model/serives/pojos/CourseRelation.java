package com.yxinmiracle.model.serives.pojos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("course_relation")
public class CourseRelation implements Serializable {

    @TableId(value = "course_relation_id",type = IdType.AUTO)
    private Integer courseRelationId;

    @TableField(value = "course_id")
    private Integer courseId;

    @TableField(value = "relation_count")
    private Integer relationCount;

    @TableField(value = "course_name")
    private String courseName;

}
