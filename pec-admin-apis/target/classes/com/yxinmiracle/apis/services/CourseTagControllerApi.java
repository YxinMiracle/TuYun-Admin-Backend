package com.yxinmiracle.apis.services;

import com.yxinmiracle.model.common.dtos.ResponseResult;
import com.yxinmiracle.model.serives.dtos.CourseTagAndTagPropertyDto;
import com.yxinmiracle.model.serives.dtos.CourseTagDto;
import com.yxinmiracle.model.serives.vos.CourseTagPropertyVo;

public interface CourseTagControllerApi {
    public ResponseResult showTagData(CourseTagDto dto);

    /**
     * 该接口用来更新知识点的名称
     * 以及
     * 给知识点添加属性
     * @param dto CourseTagAndTagPropertyDto
     * @return ResponseResult
     */
    public ResponseResult updateCourseTagNameAndProperty(CourseTagAndTagPropertyDto dto);

    /**
     * 得到tag的各种属性值
     * @param tagId 知识点的id
     * @return ResponseResult<CourseTagPropertyVo>
     */
    public ResponseResult<CourseTagPropertyVo> getCourseTagProperty(Integer tagId);

    /**
     * delete course tag by courseTagId
     * @param tagId tagId
     * @return ResponseResult
     */
    public ResponseResult deleteTag(Integer tagId);

    /**
     * resume course tag by courseTagId
     * @param tagId courseTagId
     * @return ResponseResult
     */
    public ResponseResult resumeTag(Integer tagId);

    /**
     * get all course tag to insert into select box
     * @return
     */
    public ResponseResult getAllCourseTag(Integer courseId);
}
