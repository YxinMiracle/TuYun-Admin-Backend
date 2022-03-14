package com.yxinmiracle.apis.services;

import com.yxinmiracle.model.common.dtos.ResponseResult;
import com.yxinmiracle.model.serives.dtos.CourseTagAndTagPropertyDto;
import com.yxinmiracle.model.serives.dtos.RelationDto;
import com.yxinmiracle.model.serives.dtos.ShowRelationDto;

public interface RelationControllerApi {

    public ResponseResult addRelation(RelationDto dto);

    /**
     * 更新relation数据
     * 并且neo4j数据库也需要进行更新
     * @param dto RelationDto
     * @return ResponseResult
     */
    public ResponseResult updateRelation(RelationDto dto);

    public ResponseResult showRelationCourseData(ShowRelationDto dto);

    /**
     * 根据知识点的名称和课程的id查询某个知识点的关系
     * @param tagName 知识点名称
     * @param courseId 课程id
     * @return ResponseResult
     */
    public ResponseResult getTagRelationDataByTagNameAndCourseId(String tagName,Integer courseId);



}
