package com.yxinmiracle.model.serives.pojos;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("course_teacher")
@Data
public class CourseTeacher {

    @TableField(value = "course_id")
    private Integer courseId;

    @TableField(value = "user_id")
    private Integer userId;

}
