package com.yxinmiracle.model.serives.pojos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.sun.org.apache.regexp.internal.RE;
import com.yxinmiracle.model.common.dtos.PageRequestDto;
import lombok.Data;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.Alias;
import org.springframework.lang.NonNull;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Data
@TableName("services")
public class Services implements Serializable {

    @TableId(value = "services_id",type = IdType.AUTO)
    private Integer servicesId;

    @TableField(value = "services_name")
    private String servicesName;

    @TableField(value = "info")
    private String info;

    @TableField(value = "article")
    private String article;

    @TableField(value = "image")
    private String image;

    @TableField(value = "letter")
    private char letter;

    @TableField(value = "is_show")
    private short isShow;

    @TableField(value = "is_delete")
    private short isDelete;

    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    @TableField(value = "update_time")
    private Date updateTime;

    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    @TableField(value = "create_time")
    private Date createTime;

    public boolean check(){
        if (StringUtils.isBlank(this.servicesName) || this.servicesName.length()>20){
            return false;
        }
        if (StringUtils.isBlank(this.info)){
            return false;
        }
        if (StringUtils.isBlank(this.image)){
            return false;
        }
        if (Objects.isNull(isShow)){
            return false;
        }
        return true;
    }

    @Alias("ServicesIsDeleteType")
    public enum isDeleteType{
        isDelete((short)1),notDelete((short)0);

        @Getter
        short code;

        isDeleteType(short code) {
            this.code = code;
        }
    }

    @Alias("ServicesIsShowType")
    public enum isShowType{
        isShow((short)1),notShow((short)0);

        @Getter
        short code;

        isShowType(short code) {
            this.code = code;
        }
    }



}
