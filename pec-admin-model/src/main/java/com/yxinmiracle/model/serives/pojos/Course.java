package com.yxinmiracle.model.serives.pojos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.util.Date;

@Data
public class Course implements Serializable {

    @TableId(value = "course_id",type = IdType.AUTO)
    private Integer courseId;

    @TableField(value = "course_name")
    private String courseName;

    @TableField(value = "course_category_item_id")
    private Integer courseCategoryItemId;

    @TableField(value = "services_id")
    private Integer servicesId;

    @TableField(value = "is_quality")
    private short isQuality;

    @TableField(value = "is_show")
    private short isShow;

    @TableField(value = "is_delete")
    private short isDelete;

    @TableField(value = "show_type")
    private short showType;

    @TableField(value = "image")
    private String image;

    @TableField(value = "start_time")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date startTime;

    @TableField(value = "end_time")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date endTime;

    @TableField(value = "create_time")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date createTime;

    @TableField(value = "update_time")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date updateTime;

    @TableField(value = "info_picture_list")
    private String infoPictureList;

    @TableField(value = "price")
    private Double price;

    @TableField(value = "letter")
    private char letter;

    @TableField(value = "course_article")
    private String courseArticle;

    @TableField(value = "info_type")
    private short infoType;

    @Alias("CourseIsDeleteType")
    public enum isDeleteType{
        isDelete((short)1),notDelete((short)0);

        @Getter
        short code;

        isDeleteType(short code) {
            this.code = code;
        }
    }

    @Alias("CourseIsQuality")
    public enum isQualityType{
        isQuality((short)1),notQuality((short)0);

        @Getter
        short code;

        isQualityType(short code) {
            this.code = code;
        }
    }

    @Alias("CourseIsShow")
    public enum isShowType{
        isShow((short)1),notShow((short)0);

        @Getter
        short code;

        isShowType(short code) {
            this.code = code;
        }
    }



}
