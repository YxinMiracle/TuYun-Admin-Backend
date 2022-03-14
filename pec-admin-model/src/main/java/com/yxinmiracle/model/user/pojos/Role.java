package com.yxinmiracle.model.user.pojos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@TableName("role")
@Data
public class Role {
    @TableId(value = "role_id",type = IdType.AUTO)
    private Integer roleId;

    @TableField(value = "role_name")
    private String roleName;

    @TableField(value = "role_key")
    private String roleKey;

    @TableField(value = "status")
    private short status;

    @TableField(value = "is_delete")
    private String isDelete;

    @TableField(value = "create_by")
    private Integer createBy;

    @TableField(value = "create_time")
    private Date createTime;

    @TableField(value = "update_by")
    private Integer updateBy;

    @TableField(value = "update_time")
    private Date updateTime;

    @TableField(value = "remark")
    private String remark;

    @TableField(value = "color")
    private String color;
}
