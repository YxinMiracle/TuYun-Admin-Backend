package com.yxinmiracle.course.controller.v1;

import com.alibaba.fastjson.JSON;
import com.yxinmiracle.apis.services.CourseTagControllerApi;
import com.yxinmiracle.course.service.CourseTagService;
import com.yxinmiracle.model.common.dtos.ResponseResult;
import com.yxinmiracle.model.common.enums.AppHttpCodeEnum;
import com.yxinmiracle.model.serives.dtos.CourseTagAndTagPropertyDto;
import com.yxinmiracle.model.serives.dtos.CourseTagDto;
import com.yxinmiracle.model.serives.pojos.CourseTag;
import com.yxinmiracle.model.serives.pojos.Relation;
import com.yxinmiracle.model.serives.vos.CourseTagPropertyVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/tag")
public class CourseTagController implements CourseTagControllerApi {


    @Autowired
    private CourseTagService courseTagService;

    @Override
    @PostMapping
    public ResponseResult showTagData(@RequestBody CourseTagDto dto) {
        return courseTagService.showTagData(dto);
    }



    @Override
    @PostMapping("/modify")
    public ResponseResult updateCourseTagNameAndProperty(@RequestBody CourseTagAndTagPropertyDto dto) {
        return courseTagService.updateCourseTagNameAndProperty(dto);
    }

    @Override
    @GetMapping("/property/{tagId}")
    public ResponseResult<CourseTagPropertyVo> getCourseTagProperty(@PathVariable("tagId") Integer tagId) {
        return courseTagService.getCourseTagProperty(tagId);

    }

    @Override
    @RequestMapping(value = "/handle/{tagId}",method = RequestMethod.DELETE)
    public ResponseResult deleteTag(@PathVariable("tagId") Integer tagId) {
        return courseTagService.deleteTag(tagId);
    }

    @Override
    @PostMapping("/resume/{tagId}")
    public ResponseResult resumeTag(@PathVariable("tagId") Integer tagId) {
        return courseTagService.resumeTag(tagId);
    }

    @Override
    @GetMapping("/{courseId}")
    public ResponseResult getAllCourseTag(@PathVariable("courseId") Integer courseId) {
        return courseTagService.getAllCourseTag(courseId);
    }
}
