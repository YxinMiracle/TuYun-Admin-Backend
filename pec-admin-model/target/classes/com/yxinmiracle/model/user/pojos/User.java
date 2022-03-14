package com.yxinmiracle.model.user.pojos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.yxinmiracle.utils.common.EmailUtil;
import lombok.Data;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
@TableName("user")
public class User implements Serializable {
    @TableId(value = "user_id", type = IdType.AUTO)
    private Integer userId;

    @TableField(value = "user_account")
    private String userAccount;

    @TableField(value = "username")
    private String username;

    @TableField(value = "password")
    private String password;

    @TableField(value = "phone")
    private String phone;

    @TableField(value = "email")
    private String email;

    @TableField(value = "create_time")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createTime;

    @TableField(value = "sex")
    private short sex;

    @TableField(value = "image")
    private String image;

    @TableField(value = "update_time")
    private Date updateTime;

    @TableField(value = "services_id")
    private Integer servicesId;

    @TableField(value = "user_article")
    private String userArticle;

    @TableField(value = "user_is_delete")
    private short userIsDelete;

    @Alias("UserIsDeleteEnum")
    public enum isDeleteEnum{
        isDelete((short)1),notDelete((short)2);

        @Getter
        short code;

        isDeleteEnum(short code){
            this.code = code;
        }
    }

    public boolean check() {
        if (this.userAccount == null || StringUtils.isBlank(this.userAccount)) {
            return false;
        }
        if (this.username == null || StringUtils.isBlank(this.username) || this.username.length() > 10) {
            return false;
        }

        if (StringUtils.isBlank(this.email) || EmailUtil.isNotEmail(this.email)) {
            return false;
        }

        if (StringUtils.isBlank(image)) {
            return false;
        }

        if (this.servicesId == null){
            return false;
        }

        if (this.phone.length() != 11){
            return false;
        }

        return true;
    }


}


