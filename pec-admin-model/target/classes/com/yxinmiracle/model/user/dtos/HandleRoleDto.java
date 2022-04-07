package com.yxinmiracle.model.user.dtos;

import lombok.Data;

/**
 * 处理role表的添加和编辑操作
 */
@Data
public class HandleRoleDto {
    private Integer roleId;
    private String roleName;
    private String remark;
    private String color;
}
