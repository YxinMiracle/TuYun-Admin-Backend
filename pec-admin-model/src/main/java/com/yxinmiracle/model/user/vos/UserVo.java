package com.yxinmiracle.model.user.vos;

import com.yxinmiracle.model.serives.pojos.Services;
import com.yxinmiracle.model.user.pojos.Role;
import com.yxinmiracle.model.user.pojos.User;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class UserVo extends User implements Serializable {
    private Services services;
    private List<Role> roleList;
}
