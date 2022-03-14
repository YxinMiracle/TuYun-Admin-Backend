package com.yxinmiracle.model.user.pojos;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName(value = "role_menu")
@Data
public class RoleMenu {

    @TableField(value = "role_id")
    private Integer roleId;

    @TableField(value = "menu_id")
    private Integer menuId;

}
