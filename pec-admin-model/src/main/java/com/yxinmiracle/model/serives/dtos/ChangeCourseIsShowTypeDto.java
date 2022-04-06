package com.yxinmiracle.model.serives.dtos;

import lombok.Data;

import java.util.List;

/**
 * use to change course is show type
 * exp:
 *     pojo.direction = right  => Course::getIsShow = 1
 *     pojo.direction = left  => Course::getIsShow = 0
 */
@Data
public class ChangeCourseIsShowTypeDto {

    List<Integer> courseIdList;
    short isShow;

}
