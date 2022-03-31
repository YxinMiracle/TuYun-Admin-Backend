package com.yxinmiracle.model.serives.pojos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("course_video")
public class CourseVideo {
    @TableId(value = "course_video_id",type = IdType.AUTO)
    private Integer courseVideoId;

    @TableField(value = "course_id")
    private Integer courseId;

    @TableField(value = "course_name")
    private String courseName;

    @TableField(value = "video_title")
    private String videoTitle;

    @TableField(value = "video_part")
    private Integer videoPart;
}
