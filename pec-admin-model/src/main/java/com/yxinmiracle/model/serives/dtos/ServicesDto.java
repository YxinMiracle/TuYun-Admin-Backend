package com.yxinmiracle.model.serives.dtos;

import com.yxinmiracle.model.common.dtos.PageRequestDto;
import lombok.Data;

import java.io.Serializable;

@Data
public class ServicesDto extends PageRequestDto implements Serializable {

    private String servicesName;

    private String letter;

}
