package com.yxinmiracle.model.user.pojos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.Value;

import java.util.Date;

@Data
@TableName("menu")
public class Menu {

    @TableId(value = "menu_id",type = IdType.AUTO)
    private Integer menuId;

    @TableField(value = "menu_name")
    private String menuName;

    @TableField(value = "path")
    private String path;

    @TableField(value = "component")
    private String component;

    @TableField(value = "is_visible")
    private String isVisible;

    @TableField(value = "status")
    private String status;

    @TableField(value = "perms")
    private String perms; // 表示权限

    @TableField(value = "icon")
    private String icon;

    @TableField(value = "create_by")
    private Integer createBy;

    @TableField(value = "create_time")
    private Date createTime;

    @TableField(value = "update_by")
    private Integer updateBy;

    @TableField(value = "update_time")
    private Date updateTime;

    @TableField(value = "is_delete")
    private short isDelete;

    @TableField(value = "remark")
    private String remark;
}
