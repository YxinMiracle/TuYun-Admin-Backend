package com.yxinmiracle.course.controller.v1;

import com.yxinmiracle.apis.services.RelationControllerApi;
import com.yxinmiracle.course.service.RelationService;
import com.yxinmiracle.model.common.dtos.ResponseResult;
import com.yxinmiracle.model.common.enums.AppHttpCodeEnum;
import com.yxinmiracle.model.serives.dtos.CourseTagAndTagPropertyDto;
import com.yxinmiracle.model.serives.dtos.RelationDto;
import com.yxinmiracle.model.serives.dtos.ShowRelationDto;
import com.yxinmiracle.model.serives.pojos.Relation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/relation")
public class RelationController implements RelationControllerApi {

    @Autowired
    private RelationService relationService;


    @Override
    @PostMapping("/add")
    public ResponseResult addRelation(@RequestBody RelationDto dto) {
        return relationService.addRelation(dto);
    }

    @Override
    @RequestMapping(value = "/handle",method = RequestMethod.PUT)
    public ResponseResult updateRelation(@RequestBody RelationDto dto) {
        return relationService.updateRelation(dto);
    }

    /**
     * 展示关系列表页面中的数据
     * @param dto dto
     * @return ResponseResult
     */
    @Override
    @PostMapping("/show")
    public ResponseResult showRelationCourseData(@RequestBody ShowRelationDto dto) {
        return relationService.showRelationCourseData(dto);
    }

    @Override
    @GetMapping("/{tagName}/{courseId}")
    public ResponseResult getTagRelationDataByTagNameAndCourseId(@PathVariable("tagName") String tagName,@PathVariable("courseId") Integer courseId) {
        return relationService.getTagRelationDataByTagNameAndCourseId(tagName,courseId);
    }

}
