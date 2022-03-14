package com.yxinmiracle.model.user.dtos;

import com.yxinmiracle.model.common.dtos.PageRequestDto;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class PageUserDto extends PageRequestDto implements Serializable {

     private String userAccount ;
     private String username;
     private String email;
     private String phone;
     private Date startTime;
     private Date endTime;
     private Integer roleId;
     private Integer servicesId;

}
