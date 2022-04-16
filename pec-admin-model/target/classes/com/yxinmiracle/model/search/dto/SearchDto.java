package com.yxinmiracle.model.search.dto;

import com.yxinmiracle.model.common.dtos.PageRequestDto;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchDto extends PageRequestDto implements Serializable {
    private String keyWord;

}
