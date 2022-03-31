package com.yxinmiracle.model.serives.pojos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("course_video_count")
public class CourseVideoCount implements Serializable {
    @TableId(value = "course_video_count_id",type = IdType.AUTO)
    private Integer courseVideoCountId;

    @TableField(value = "course_id")
    private Integer courseId;

    @TableField(value = "course_name")
    private String courseName;

    @TableField(value = "course_video_count")
    private Integer courseVideoCount;
}
