package com.yxinmiracle.model.serives.pojos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.util.Date;

/**
 * 用作描述课程内容的标签，不是知识点
 */
@Data
@TableName("course_tag")
public class CourseTag implements Serializable {

    @TableId(value = "course_tag_id",type = IdType.AUTO)
    private Integer courseTagId;

    @TableField(value = "course_id")
    private Integer courseId;

    @TableField(value = "course_name")
    private String courseName;

    @TableField(value = "tag_name")
    private String tagName;

    @TableField(value = "tag_desc")
    private String tagDesc;

    @TableField(value = "property_json")
    private String propertyJson;

    @TableField(value = "course_tag_is_show")
    private short courseTagIsShow;

    @TableField(value = "course_tag_is_delete")
    private short courseTagIsDelete;

    @TableField(value = "course_tag_create_time")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date courseTagCreateTime;

    @TableField(value = "course_tag_update_time")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date courseTagUpdateTime;

    @TableField(value = "course_tag_update_user_id")
    private Integer courseTagUpdateUserId;



    @Alias("courseTagIsShowType")
    public enum isShowType{
        isShow((short)1),notShow((short)0);

        @Getter
        short code;

        isShowType(short code){
            this.code = code;
        }
    }

    @Alias("courseTagIsDeleteType")
    public enum isDeleteType{
        isDelete((short)1),notDelete((short)0);

        @Getter
        short code;

        isDeleteType(short code){
            this.code = code;
        }
    }

}
