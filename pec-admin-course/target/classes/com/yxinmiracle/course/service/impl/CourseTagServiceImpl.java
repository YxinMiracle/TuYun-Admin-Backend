package com.yxinmiracle.course.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yxinmiracle.course.mapper.CourseTagMapper;
import com.yxinmiracle.course.service.CourseTagService;
import com.yxinmiracle.model.common.dtos.PageResponseResult;
import com.yxinmiracle.model.common.dtos.ResponseResult;
import com.yxinmiracle.model.common.enums.AppHttpCodeEnum;
import com.yxinmiracle.model.serives.dtos.CourseTagAndTagPropertyDto;
import com.yxinmiracle.model.serives.dtos.CourseTagDto;
import com.yxinmiracle.model.serives.pojos.CourseTag;
import com.yxinmiracle.model.serives.vos.CourseTagPropertyVo;
import com.yxinmiracle.model.serives.vos.CourseTagVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class CourseTagServiceImpl implements CourseTagService {
    @Autowired
    private CourseTagMapper courseTagMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${mq.graph.exchange}")
    private String neo4jExchange;

    @Value("${mq.graph.routing.updateTagPropertyRouting}")
    private String updateTagPropertyRouting;

    @Value("${mq.graph.routing.deleteTagRouting}")
    private String deleteTagRouting;

    @Value("${mq.graph.routing.resumeTagRouting}")
    private String resumeTagRouting;

    @Override
    public ResponseResult showTagData(CourseTagDto dto) {
        List<CourseTagVo> courseTagVos = courseTagMapper.showTagData(dto);
        Integer total = courseTagMapper.showTagDataCount(dto);
        ResponseResult responseResult = new PageResponseResult(dto.getPage(), dto.getSize(), total);
        responseResult.setData(courseTagVos);
        responseResult.setCode(200);
        responseResult.setErrorMessage("请求成功");
        return responseResult;
    }

    @Override
    public ResponseResult<CourseTagPropertyVo> getCourseTagProperty(Integer tagId) {
        CourseTag courseTag = courseTagMapper.selectById(tagId);
        CourseTagPropertyVo courseTagPropertyVo = new CourseTagPropertyVo();
        courseTagPropertyVo.setCourseName(courseTag.getCourseName());
        courseTagPropertyVo.setCourseTagId(courseTag.getCourseTagId());
        courseTagPropertyVo.setTagDesc(courseTag.getTagDesc());
        Map<String, String> propertyJson = JSON.parseObject(courseTag.getPropertyJson(), Map.class);
        courseTagPropertyVo.setPropertyMap(propertyJson);
        return ResponseResult.okResult(courseTagPropertyVo);
    }

    @Override
    public ResponseResult deleteTag(Integer tagId) {
        if (Objects.isNull(tagId)){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        LambdaUpdateWrapper<CourseTag> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(CourseTag::getCourseTagId,tagId).set(CourseTag::getCourseTagIsDelete,CourseTag.isDeleteType.isDelete.getCode());
        courseTagMapper.update(null,lambdaUpdateWrapper);
        rabbitTemplate.convertAndSend(neo4jExchange, deleteTagRouting, tagId);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult resumeTag(Integer tagId) {
        if (Objects.isNull(tagId)){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        LambdaUpdateWrapper<CourseTag> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(CourseTag::getCourseTagId,tagId).set(CourseTag::getCourseTagIsDelete,CourseTag.isDeleteType.notDelete.getCode());
        courseTagMapper.update(null,lambdaUpdateWrapper);
        rabbitTemplate.convertAndSend(neo4jExchange, resumeTagRouting, tagId);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getAllCourseTag(Integer courseId) {
        List<CourseTag> courseTags = courseTagMapper.selectList(Wrappers.<CourseTag>lambdaQuery().eq(CourseTag::getCourseId,courseId));
        return ResponseResult.okResult(courseTags);
    }

    @Override
    @Transactional
    public ResponseResult updateCourseTagNameAndProperty(CourseTagAndTagPropertyDto dto) {
        if (Objects.isNull(dto)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        CourseTag courseTag = dto.getNewTagData();
        if (StringUtils.isBlank(courseTag.getTagName())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        if (StringUtils.isBlank(courseTag.getTagDesc())){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        CourseTag databaseCourseTag = courseTagMapper.selectById(courseTag.getCourseTagId());
        if (!databaseCourseTag.getCourseName().equals(courseTag.getCourseName())){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        /**
         * 取出 userId
         */
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        Integer userId = (Integer) authentication.getPrincipal();

        LambdaUpdateWrapper<CourseTag> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(CourseTag::getCourseTagId, courseTag.getCourseTagId()).set(CourseTag::getTagName, courseTag.getTagName()).set(CourseTag::getTagDesc,courseTag.getTagDesc()).set(CourseTag::getCourseTagUpdateTime, new Date()).set(CourseTag::getCourseTagUpdateUserId, userId);
        String propertyMapString = JSON.toJSONString(dto.getRetPropertyMap());
        if (Objects.nonNull(dto.getRetPropertyMap()) && StringUtils.isNotBlank(propertyMapString)) {
            lambdaUpdateWrapper.set(CourseTag::getPropertyJson, propertyMapString);
        }
        courseTagMapper.update(null, lambdaUpdateWrapper);
        rabbitTemplate.convertAndSend(neo4jExchange, updateTagPropertyRouting, dto);
        return ResponseResult.okResult();
    }


}
