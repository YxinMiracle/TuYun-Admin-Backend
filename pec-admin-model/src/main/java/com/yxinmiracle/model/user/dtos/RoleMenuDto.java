package com.yxinmiracle.model.user.dtos;

import lombok.Data;

import java.util.List;

@Data
public class RoleMenuDto {

    private Integer roleId;
    private List<Integer> menuIdList;

}
