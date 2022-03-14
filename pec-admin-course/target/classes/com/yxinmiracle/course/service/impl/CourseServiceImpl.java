package com.yxinmiracle.course.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yxinmiracle.course.mapper.*;
import com.yxinmiracle.course.service.CourseService;
import com.yxinmiracle.model.common.dtos.PageResponseResult;
import com.yxinmiracle.model.common.dtos.ResponseResult;
import com.yxinmiracle.model.common.enums.AppHttpCodeEnum;
import com.yxinmiracle.model.serives.dtos.CourseDto;
import com.yxinmiracle.model.serives.dtos.HandleCourseDto;
import com.yxinmiracle.model.serives.pojos.*;
import com.yxinmiracle.model.serives.vos.CourseVo;
import com.yxinmiracle.utils.common.ImageUrlUtil;
import com.yxinmiracle.utils.common.JsoupUtil;
import com.yxinmiracle.utils.common.PinyinUtil;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
public class CourseServiceImpl implements CourseService {
    Logger logger = LoggerFactory.getLogger(CourseServiceImpl.class);

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private CourseTeacherMapper courseTeacherMapper;

    @Autowired
    private RelationMapper relationMapper;

    @Autowired
    private CourseRelationMapper courseRelationMapper;

    @Autowired
    private RecordCourseQuestionMapper recordCourseQuestionMapper;

    @Value("${host}")
    private String HOST;

    @Override
    public ResponseResult getCourse(CourseDto dto) {
        if (Objects.isNull(dto)){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        dto.checkParam();
        if (StringUtils.isBlank(dto.getIsQuality()+"") || Objects.isNull(dto.getIsQuality())){
            dto.setIsQuality((short)-1);
        }
        if (Objects.isNull(dto.getCourseCategoryItemId())){
            dto.setCourseCategoryItemId(-1);
        }
        List<CourseVo> courseList = courseMapper.getCourseList(dto);
        Integer courseListCount = courseMapper.getCourseListCount(dto);
        ResponseResult result = new PageResponseResult(dto.getPage(),dto.getSize(),courseListCount);
        result.setHost(HOST);
        result.setData(courseList);
        result.setCode(200);
        result.setErrorMessage("请求成功");
        return result;
    }

    @Override
    @Transactional
    public ResponseResult addCourse(HandleCourseDto dto) {
        logger.info("接收到添加课程的数据，{}",dto);
        if (Objects.isNull(dto)){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        if(!dto.check()){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        dto.setImage(ImageUrlUtil.repalceImageUrl(dto.getImage(), HOST));
        dto.setImageList(dto.getImageList().stream().map(imageStr -> ImageUrlUtil.repalceImageUrl(imageStr, HOST)).collect(Collectors.toList()));
        Course course = new Course();

        course.setCourseName(dto.getCourseName());
        course.setCourseCategoryItemId(dto.getCourseCategoryItemId());
        course.setPrice(dto.getPrice());
        course.setServicesId(dto.getServicesId());
        course.setIsQuality(dto.getIsQuality());
        course.setIsShow(dto.getIsShow());
        course.setStartTime(dto.getStartTime());
        course.setEndTime(dto.getEndTime());
        course.setCreateTime(new Date());
        course.setUpdateTime(new Date());
        course.setImage(dto.getImage());
        course.setLetter(PinyinUtil.getPinYinHeadChar(dto.getCourseName()));
        course.setCourseArticle(JsoupUtil.clean(dto.getCourseArticle()));
        course.setInfoPictureList(JSON.toJSONString(dto.getImageList()));
        course.setInfoType(dto.getInfoType());
        courseMapper.insert(course);

        CourseRelation courseRelation = new CourseRelation();
        courseRelation.setCourseId(course.getCourseId());
        courseRelation.setCourseName(course.getCourseName());
        courseRelation.setRelationCount(0);
        courseRelationMapper.insert(courseRelation);

        RecordCourseQuestion recordCourseQuestion = new RecordCourseQuestion();
        recordCourseQuestion.setCourseName(course.getCourseName());
        recordCourseQuestion.setCourseId(course.getCourseId());
        recordCourseQuestion.setCourseQuestionCount(0);
        recordCourseQuestion.setSingleChoiceQuestionsCount(0);
        recordCourseQuestion.setMultipleChoiceQuestionsCount(0);
        recordCourseQuestion.setFillTheBlanksQuestionsCount(0);
        recordCourseQuestion.setAnswerQuestionsCount(0);
        recordCourseQuestionMapper.insert(recordCourseQuestion);

        // ======================== 插入 course_teacher数据
        CourseTeacher courseTeacher = new CourseTeacher();
        for (Integer teacherId : dto.getTeacherIdList()) {
            courseTeacher.setCourseId(course.getCourseId());
            courseTeacher.setUserId(teacherId);
            courseTeacherMapper.insert(courseTeacher);
        }

        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult updateCourse(HandleCourseDto dto) {
        logger.info("接收到更新课程的数据，{}",dto);
        if (Objects.isNull(dto)){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        if(!dto.check()){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        dto.setImage(ImageUrlUtil.repalceImageUrl(dto.getImage(), HOST));
        dto.setImageList(dto.getImageList().stream().map(imageStr -> ImageUrlUtil.repalceImageUrl(imageStr, HOST)).collect(Collectors.toList()));
        Course course = new Course();
        course.setCourseId(dto.getCourseId());
        course.setCourseName(dto.getCourseName());
        course.setCourseCategoryItemId(dto.getCourseCategoryItemId());
        course.setPrice(dto.getPrice());
        course.setServicesId(dto.getServicesId());
        course.setIsQuality(dto.getIsQuality());
        course.setIsShow(dto.getIsShow());
        course.setStartTime(dto.getStartTime());
        course.setEndTime(dto.getEndTime());
        course.setCreateTime(new Date());
        course.setUpdateTime(new Date());
        course.setImage(dto.getImage());
        course.setLetter(PinyinUtil.getPinYinHeadChar(dto.getCourseName()));
        course.setCourseArticle(JsoupUtil.clean(dto.getCourseArticle()));
        course.setInfoPictureList(JSON.toJSONString(dto.getImageList()));
        course.setInfoType(dto.getInfoType());
        courseMapper.updateById(course);

        courseTeacherMapper.delete(Wrappers.<CourseTeacher>lambdaQuery().eq(CourseTeacher::getCourseId,dto.getCourseId()));
        for (int i = 0; i < dto.getTeacherIdList().size(); i++) {
            CourseTeacher courseTeacher = new CourseTeacher();
            courseTeacher.setUserId(dto.getTeacherIdList().get(i));
            courseTeacher.setCourseId(dto.getCourseId());
            courseTeacherMapper.insert(courseTeacher);
        }

        return ResponseResult.okResult();
    }

    @Override
    @Transactional
    public ResponseResult deleteCourse(Integer courseId) {
        LambdaUpdateWrapper<Course> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(Course::getCourseId,courseId).set(Course::getIsDelete,Course.isDeleteType.isDelete.getCode());
        courseMapper.update(null,lambdaUpdateWrapper);
        return ResponseResult.okResult();
//        courseTeacherMapper.delete(Wrappers.<CourseTeacher>lambdaQuery().eq(CourseTeacher::getCourseId,dto.getCourseId()));

    }

    @Override
    public ResponseResult<List<Course>> getAllCourse() {
        List<Course> courses = courseMapper.selectList(null);
        return ResponseResult.okResult(courses);
    }
}
