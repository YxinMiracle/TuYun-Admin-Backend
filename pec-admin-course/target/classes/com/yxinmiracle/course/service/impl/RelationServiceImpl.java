package com.yxinmiracle.course.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yxinmiracle.course.mapper.CourseMapper;
import com.yxinmiracle.course.mapper.CourseRelationMapper;
import com.yxinmiracle.course.mapper.CourseTagMapper;
import com.yxinmiracle.course.mapper.RelationMapper;
import com.yxinmiracle.course.service.RelationService;
import com.yxinmiracle.model.common.dtos.PageResponseResult;
import com.yxinmiracle.model.common.dtos.ResponseResult;
import com.yxinmiracle.model.common.enums.AppHttpCodeEnum;
import com.yxinmiracle.model.graph.AddNodeAndRelationDto;
import com.yxinmiracle.model.graph.ConnectNode;
import com.yxinmiracle.model.graph.Node;
import com.yxinmiracle.model.graph.NodeAndConnectNode;
import com.yxinmiracle.model.serives.dtos.RelationDto;
import com.yxinmiracle.model.serives.dtos.ShowRelationDto;
import com.yxinmiracle.model.serives.pojos.Course;
import com.yxinmiracle.model.serives.pojos.CourseRelation;
import com.yxinmiracle.model.serives.pojos.CourseTag;
import com.yxinmiracle.model.serives.pojos.Relation;
import com.yxinmiracle.model.serives.vos.ShowRelationCourseVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class RelationServiceImpl implements RelationService {
    Logger logger = LoggerFactory.getLogger(RelationServiceImpl.class);

    @Autowired
    private RelationMapper relationMapper;

    @Autowired
    private CourseMapper courseMapper;

    @Value("${host}")
    private String HOST;

    @Autowired
    private CourseTagMapper courseTagMapper;

    @Autowired
    private CourseRelationMapper courseRelationMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${mq.graph.exchange}")
    private String neo4jExchange;

    @Value("${mq.graph.routing.graphAddRelationAndNodeRouting}")
    private String neo4jAddRelationAndNodeRouting;

    @Value("${mq.graph.routing.updateRelationNameRouting}")
    private String updateRelationNameRouting;

    @Override
    @Transactional
    public ResponseResult addRelation(RelationDto dto) {
        if (dto.getCourseId() <= 0) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        if (dto.getRelationDataList().size() <= 0) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "没有关系数据");
        }
        if (StringUtils.isBlank(dto.getCourseName())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "课程名称出现错误");
        }
        if (!dto.check()){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        Course course = courseMapper.selectOne(Wrappers.<Course>lambdaQuery().eq(Course::getCourseId, dto.getCourseId()));
        if (!course.getCourseName().equals(dto.getCourseName())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "课程名称出现错误");
        }


        List<Relation> relationDataList = dto.getRelationDataList(); // 前端获取到的关系数据


        List<Node> nodeList = new ArrayList<>(); // 知识点节点
        List<ConnectNode> connectNodeList = new ArrayList<>(); // ConnectNode为存储关系的节点

        Set<String> tagSet = new HashSet<>(); // 知识点
        for (Relation relation : relationDataList) {
            // 1.将tagName放入set中
            tagSet.add(relation.getKnowledgePointStartName());
            tagSet.add(relation.getKnowledgePointEndName());
            // 2.构造relation
            relation.setCourseId(dto.getCourseId());
            relation.setCourseName(dto.getCourseName());
            relation.setRelationCreateTime(new Date());
            relation.setRelationUpdateTime(new Date());
            relation.setRelationIsShow((short) 1);
            relation.setRelationIsDelete((short) 0);
            // 3.添加进数据库
            relationMapper.insert(relation);
            connectNodeList.add(new ConnectNode(relation.getCourseName() + "_relation", relation.getKnowledgePointStartName(), relation.getRelationName(), relation.getKnowledgePointEndName(), relation.getCourseName()));
        }


        /**
         * 更新courseRelation表
         * 在这个时候添加是为了保证前面的操作没有错误
         * 不过可以回滚
         */
        CourseRelation courseRelation = courseRelationMapper.selectOne(Wrappers.<CourseRelation>lambdaQuery().eq(CourseRelation::getCourseId, dto.getCourseId()));
        Integer oldRelationCount = courseRelation.getRelationCount();
        Integer newRelationCount = oldRelationCount + relationDataList.size();
        LambdaUpdateWrapper<CourseRelation> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(CourseRelation::getCourseId, dto.getCourseId()).set(CourseRelation::getCourseRelationId,newRelationCount);
        courseRelationMapper.update(null, lambdaUpdateWrapper);

        /**
         * 取出 userId
         */
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        Integer userId = (Integer) authentication.getPrincipal();

        /**
         * 1.构造 courseTag 并添加进数据库
         * 2.构造 nodeList 进行图数据库的添加
         */
        for (String tagName : tagSet) {
            CourseTag courseTag = new CourseTag();
            courseTag.setCourseId(dto.getCourseId());
            courseTag.setCourseName(dto.getCourseName());
            courseTag.setTagName(tagName);
            courseTag.setCourseTagIsShow(CourseTag.isShowType.isShow.getCode());
            courseTag.setCourseTagIsDelete(CourseTag.isDeleteType.notDelete.getCode());
            courseTag.setCourseTagCreateTime(new Date());
            courseTag.setCourseTagUpdateTime(new Date());
            courseTag.setCourseTagUpdateUserId(userId);
            try {
                courseTagMapper.insert(courseTag);
                nodeList.add(new Node(dto.getCourseName(), tagName, "", dto.getCourseName(), Node.isDeleteEnum.notDelete.getCode(), dto.getCourseId(), courseTag.getCourseTagId()));
            } catch (Exception e) {
                logger.error("出现了已经存在的tag");
            }

        }

        /**
         * 构造 NodeAndConnectNode
         * 里面存储的内容为：
         * 1. className + "_relation"
         * 2. className
         * 目的是为了将关系构建起来
         */
        NodeAndConnectNode nodeAndConnectNode = new NodeAndConnectNode(dto.getCourseName(), dto.getCourseName() + "_relation");
        AddNodeAndRelationDto addNodeAndRelationDto = new AddNodeAndRelationDto();
        addNodeAndRelationDto.setNode(nodeList);
        addNodeAndRelationDto.setConnectNode(connectNodeList);
        addNodeAndRelationDto.setNodeAndConnectNode(nodeAndConnectNode);
        logger.info("即将往rabbitMq发送信息，进行图数据库的添加，数据为{}", addNodeAndRelationDto);
        rabbitTemplate.convertAndSend(neo4jExchange, neo4jAddRelationAndNodeRouting, addNodeAndRelationDto);
        return ResponseResult.okResult();
    }

    /**
     * 关系列表页面的数据
     * 页面uri : /relation/list
     *
     * @param dto ShowRelationDto
     * @return ResponseResult
     */
    @Override
    public ResponseResult showRelationCourseData(ShowRelationDto dto) {

        if (Objects.isNull(dto)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        IPage<CourseRelation> page = new Page<>(dto.getPage(), dto.getSize());
        LambdaQueryWrapper<CourseRelation> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        if (StringUtils.isNotBlank(dto.getCourseName())) {
            lambdaQueryWrapper.like(CourseRelation::getCourseName, dto.getCourseName());
        }

        IPage<CourseRelation> courseRelationIPage = courseRelationMapper.selectPage(page, lambdaQueryWrapper);
        ResponseResult responseResult = new PageResponseResult(dto.getPage(), dto.getSize(), (int) courseRelationIPage.getTotal());
        responseResult.setData(courseRelationIPage.getRecords());
        responseResult.setErrorMessage("请求成功");
        responseResult.setCode(AppHttpCodeEnum.SUCCESS.getCode());
        responseResult.setHost(HOST);
        return responseResult;
    }

    @Override
    public ResponseResult getTagRelationDataByTagNameAndCourseId(String tagName, Integer courseId) {
        if (StringUtils.isBlank(tagName)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        if (Objects.isNull(courseId)){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        LambdaUpdateWrapper<Relation> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(Relation::getCourseId, courseId);
        lambdaUpdateWrapper.and(wrapper -> wrapper.eq(Relation::getKnowledgePointStartName, tagName).or().eq(Relation::getKnowledgePointEndName, tagName));
        List<Relation> relations = relationMapper.selectList(lambdaUpdateWrapper);
        return ResponseResult.okResult(relations);
    }

    /**
     * 更新relation数据
     * @param dto RelationDto
     * @return ResponseResult
     */
    @Override
    public ResponseResult updateRelation(RelationDto dto) {
        if (Objects.isNull(dto)){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        if (!dto.check()){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        Course course = courseMapper.selectOne(Wrappers.<Course>lambdaQuery().eq(Course::getCourseId, dto.getCourseId()));
        if (!course.getCourseName().equals(dto.getCourseName())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        List<Relation> relationDataList = dto.getRelationDataList();
        for (Relation relation : relationDataList) {
            LambdaUpdateWrapper<Relation> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            lambdaUpdateWrapper
                    .eq(Relation::getCourseId,dto.getCourseId())
                    .eq(Relation::getCourseName,dto.getCourseName())
                    .eq(Relation::getKnowledgePointStartName,relation.getKnowledgePointStartName())
                    .eq(Relation::getKnowledgePointEndName,relation.getKnowledgePointEndName())
                    .set(Relation::getRelationName,relation.getRelationName())
                    .set(Relation::getRelationUpdateTime,new Date());
            relationMapper.update(null,lambdaUpdateWrapper);
        }

        rabbitTemplate.convertAndSend(neo4jExchange, updateRelationNameRouting, dto);
        return ResponseResult.okResult();
    }
}
