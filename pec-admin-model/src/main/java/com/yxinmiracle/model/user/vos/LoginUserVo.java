package com.yxinmiracle.model.user.vos;

import com.yxinmiracle.model.user.pojos.User;
import lombok.Data;

@Data
public class LoginUserVo extends User {
    private String token;
}
