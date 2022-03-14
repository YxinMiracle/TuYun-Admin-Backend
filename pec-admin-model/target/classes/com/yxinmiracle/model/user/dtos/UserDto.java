package com.yxinmiracle.model.user.dtos;

import com.yxinmiracle.model.user.pojos.User;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class UserDto extends User implements Serializable {
    private List<Integer> roleIdList;
}
