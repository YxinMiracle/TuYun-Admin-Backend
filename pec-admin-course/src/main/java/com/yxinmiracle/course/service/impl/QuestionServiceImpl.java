package com.yxinmiracle.course.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yxinmiracle.course.mapper.*;
import com.yxinmiracle.course.service.QuestionService;
import com.yxinmiracle.model.common.dtos.PageResponseResult;
import com.yxinmiracle.model.common.dtos.ResponseResult;
import com.yxinmiracle.model.common.enums.AppHttpCodeEnum;
import com.yxinmiracle.model.search.es.EsQuestion;
import com.yxinmiracle.model.serives.dtos.HandleQuestionDto;
import com.yxinmiracle.model.serives.dtos.QuestionDto;
import com.yxinmiracle.model.serives.pojos.*;
import com.yxinmiracle.model.serives.vos.ChoiceQuestionVo;
import com.yxinmiracle.model.serives.vos.QuestionEchartsVo;
import com.yxinmiracle.model.user.pojos.User;
import com.yxinmiracle.utils.common.JsoupUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class QuestionServiceImpl implements QuestionService {

    Logger logger = LoggerFactory.getLogger(QuestionServiceImpl.class);

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private RecordCourseQuestionMapper recordCourseQuestionMapper;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionTagMapper questionTagMapper;

    @Autowired
    private QuestionItemMapper questionItemMapper;

    @Autowired
    private CourseTagMapper courseTagMapper;

    @Value("${host}")
    private String HOST;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${mq.search.searchExchange}")
    private String searchExchange;

    @Value("${mq.search.routing.addChoiceQuestionRouting}")
    private String addChoiceQuestionRouting;

    @Value("${mq.search.routing.addAnswerQuestionRouting}")
    private String addAnswerQuestionRouting;

    @Override
    public ResponseResult getQuestionData(QuestionDto dto) {
        if (Objects.isNull(dto)){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        Integer courseId = dto.getCourseId();
        if (Objects.isNull(courseId)){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        List<ChoiceQuestionVo> choiceQuestionList = questionMapper.getChoiceList(dto);
        for (ChoiceQuestionVo choiceQuestionVo : choiceQuestionList) {
            Integer questionId = choiceQuestionVo.getQuestionId();
            List<QuestionTag> questionTags = questionTagMapper.selectList(Wrappers.<QuestionTag>lambdaQuery().eq(QuestionTag::getQuestionId, questionId));
            List<CourseTag> courseTagList = new ArrayList<>();
            for (QuestionTag questionTag : questionTags) {
                CourseTag courseTag = courseTagMapper.selectById(questionTag.getCourseTagId());
                courseTagList.add(courseTag);
            }
            choiceQuestionVo.setCourseTagList(courseTagList);
        }
        Integer total = questionMapper.getChoiceListCount(dto);
        ResponseResult responseResult = new PageResponseResult(dto.getPage(),dto.getSize(),total);
        responseResult.setData(choiceQuestionList);
        responseResult.setCode(AppHttpCodeEnum.SUCCESS.getCode());
        responseResult.setErrorMessage(AppHttpCodeEnum.SUCCESS.getErrorMessage());
        responseResult.setHost(HOST);
        return responseResult;
    }

    @Override
    public ResponseResult<List<QuestionEchartsVo>> getQuestionEchartsData(Integer courseId) {
        if (Objects.isNull(courseId)){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        RecordCourseQuestion recordCourseQuestion = recordCourseQuestionMapper.selectOne(Wrappers.<RecordCourseQuestion>lambdaQuery().eq(RecordCourseQuestion::getCourseId, courseId));
        List<QuestionEchartsVo> questionEchartsVoList = new ArrayList<>();
        questionEchartsVoList.add(new QuestionEchartsVo(recordCourseQuestion.getSingleChoiceQuestionsCount(),"单选题"));
        questionEchartsVoList.add(new QuestionEchartsVo(recordCourseQuestion.getMultipleChoiceQuestionsCount(),"多选题"));
        questionEchartsVoList.add(new QuestionEchartsVo(recordCourseQuestion.getFillTheBlanksQuestionsCount(),"填空题"));
        questionEchartsVoList.add(new QuestionEchartsVo(recordCourseQuestion.getAnswerQuestionsCount(),"解答题"));
        return ResponseResult.okResult(questionEchartsVoList);
    }

    @Override
    @Transactional
    public ResponseResult addQuestion(HandleQuestionDto dto) {
        if (Objects.isNull(dto)){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        Integer courseId = dto.getCourseId();
        if (Objects.isNull(courseId)){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        Course course = courseMapper.selectById(courseId);
        if (Objects.isNull(course)){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        short type = dto.getType();

        // 更新课程各类型题目的数量
        updateRecordQuestionData(type,courseId);

        if (type == Question.questionTypeEnum.singleChoiceQuestion.getCode() || type == Question.questionTypeEnum.multipleChoiceQuestion.getCode()){
            /**
             * this branch was used to set question which type is equals 1 or 2
             */
            return addChoiceQuestion(dto,course);
        }
        if (type == Question.questionTypeEnum.fillTheBlanksQuestion.getCode() || type == Question.questionTypeEnum.answerQuestion.getCode()){
            /**
             * this branch was used to set question which type is equals 4 or 5
             */
            return addAnswerQuestion(dto,course);
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.SERVER_ERROR);
    }



    private ResponseResult addChoiceQuestion(HandleQuestionDto dto,Course course) {
        if (!dto.check()){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        Question question = insertQuestionAndQuestionTag(dto, course);
        if (Objects.isNull(question)){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        List<QuestionItem> questionItemList = dto.getQuestionItemList();
        for (QuestionItem questionItem : questionItemList) {
            if (questionItem.getQuestionItemContent() != null && StringUtils.isNotBlank(questionItem.getQuestionItemContent())){
                questionItem.setQuestionId(question.getQuestionId());
                questionItem.setImage(questionItem.getImage().startsWith(HOST)?questionItem.getImage().replace(HOST,""):null);
                questionItemMapper.insert(questionItem);
            }
        }

        // 构建Mq信息，进行发送，进行在Es中存储
        EsQuestion esQuestion = new EsQuestion();
        esQuestion.setQuestion(question);
        esQuestion.setQuestionItemList(questionItemList);
        esQuestion.setQuestionTagIds(dto.getCourseTagIdList());
        //向队列中发送消息，进行往Es数据库中发送数据，（选择题，包括选项）
        rabbitTemplate.convertAndSend(searchExchange, addChoiceQuestionRouting, esQuestion);
        return ResponseResult.okResult();
    }


    private ResponseResult addAnswerQuestion(HandleQuestionDto dto,Course course) {
        if (!dto.check()){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        Question question = insertQuestionAndQuestionTag(dto, course);
        if (Objects.isNull(question)){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        // 构建Mq信息，进行发送，进行在Es中存储
        EsQuestion esQuestion = new EsQuestion();
        esQuestion.setQuestion(question);
        esQuestion.setQuestionTagIds(dto.getCourseTagIdList());
        //向队列中发送消息，进行往Es数据库中发送数据，（选择题，包括选项）
        rabbitTemplate.convertAndSend(searchExchange, addAnswerQuestionRouting, esQuestion);

        return ResponseResult.okResult();
    }

    /**
     * set question and question tag way is public
     * so change to a method
     * @param dto HandleQuestionDto
     * @param course Course
     * @return Question
     */
    private Question insertQuestionAndQuestionTag(HandleQuestionDto dto, Course course) {
        /**
         * get userId
         */
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        Integer userId = (Integer) authentication.getPrincipal();
        User user = userMapper.selectById(userId);

        Question question = new Question();
        BeanUtils.copyProperties(dto,question);
        question.setSubject(JsoupUtil.clean(dto.getSubject()));
        question.setAnalysis(JsoupUtil.clean(dto.getAnalysis()));
        question.setCourseName(course.getCourseName());
        question.setQuestionCreateTime(new Date());
        question.setQuestionUpdateTime(new Date());
        question.setCreateQuestionUserId(user.getUserId());
        question.setCreateQuestionUserName(user.getUsername());
        questionMapper.insert(question);

        if (dto.getCourseTagIdList().size()!=0){
            List<Integer> courseTagIdList = dto.getCourseTagIdList();
            for (Integer courseTagId : courseTagIdList) {
                QuestionTag questionTag = new QuestionTag();
                questionTag.setCourseTagId(courseTagId);
                questionTag.setQuestionId(question.getQuestionId());
                questionTagMapper.insert(questionTag);
            }
        }

        return question;
    }

    private void updateRecordQuestionData(short type,Integer courseId) {
        RecordCourseQuestion recordCourseQuestion = recordCourseQuestionMapper.selectOne(Wrappers.<RecordCourseQuestion>lambdaQuery().eq(RecordCourseQuestion::getCourseId, courseId));
        LambdaUpdateWrapper<RecordCourseQuestion> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        if (type == Question.questionTypeEnum.singleChoiceQuestion.getCode()){
            // 单选题
            lambdaUpdateWrapper.eq(RecordCourseQuestion::getCourseId,courseId).set(RecordCourseQuestion::getSingleChoiceQuestionsCount,recordCourseQuestion.getSingleChoiceQuestionsCount()+1);
        }
        if (type == Question.questionTypeEnum.multipleChoiceQuestion.getCode()){
            // 多选题
            lambdaUpdateWrapper.eq(RecordCourseQuestion::getCourseId,courseId).set(RecordCourseQuestion::getMultipleChoiceQuestionsCount,recordCourseQuestion.getMultipleChoiceQuestionsCount()+1);
        }
        if (type == Question.questionTypeEnum.fillTheBlanksQuestion.getCode()){
            // 填空题
            lambdaUpdateWrapper.eq(RecordCourseQuestion::getCourseId,courseId).set(RecordCourseQuestion::getFillTheBlanksQuestionsCount,recordCourseQuestion.getSingleChoiceQuestionsCount()+1);
        }
        if (type == Question.questionTypeEnum.answerQuestion.getCode()){
            // 解答题
            lambdaUpdateWrapper.eq(RecordCourseQuestion::getCourseId,courseId).set(RecordCourseQuestion::getAnswerQuestionsCount,recordCourseQuestion.getSingleChoiceQuestionsCount()+1);
        }
        recordCourseQuestionMapper.update(null,lambdaUpdateWrapper);
    }



    @Override
    @Transactional
    public ResponseResult updateQuestion(HandleQuestionDto dto) {
        if (Objects.isNull(dto)){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        Integer courseId = dto.getCourseId();
        if (Objects.isNull(courseId)){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        Course course = courseMapper.selectById(courseId);
        if (Objects.isNull(course)){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        Integer userId = (Integer) authentication.getPrincipal();
        User user = userMapper.selectById(userId);

        Question question = new Question();
        BeanUtils.copyProperties(dto,question);
        question.setSubject(JsoupUtil.clean(dto.getSubject()));
        question.setAnalysis(JsoupUtil.clean(dto.getAnalysis()));
        question.setCourseName(course.getCourseName());
        question.setQuestionUpdateTime(new Date());
        question.setCreateQuestionUserId(user.getUserId());
        question.setCreateQuestionUserName(user.getUsername());
        questionMapper.updateById(question);

        List<QuestionItem> questionItemList = dto.getQuestionItemList();
        for (QuestionItem questionItem : questionItemList) {
            questionItem.setImage(questionItem.getImage().startsWith(HOST)?questionItem.getImage().replace(HOST,""):null);
            questionItem.setQuestionItemContent(JsoupUtil.clean(questionItem.getQuestionItemContent()));
            questionItemMapper.updateById(questionItem);
        }
        questionTagMapper.delete(Wrappers.<QuestionTag>lambdaQuery().eq(QuestionTag::getQuestionId,question.getQuestionId()));
        List<Integer> courseTagIdList = dto.getCourseTagIdList();
        for (Integer courseTagId : courseTagIdList) {
            QuestionTag questionTag = new QuestionTag();
            questionTag.setQuestionId(question.getQuestionId());
            questionTag.setCourseTagId(courseTagId);
            questionTagMapper.insert(questionTag);
        }
        return ResponseResult.okResult();
    }

    @Override
    @Transactional
    public ResponseResult deleteQuestion(Integer questionId) {
        if (questionId == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        Question question = questionMapper.selectById(questionId);
        Integer courseId = question.getCourseId();
        short questionType = question.getType();
        RecordCourseQuestion recordCourseQuestion = recordCourseQuestionMapper.selectOne(Wrappers.<RecordCourseQuestion>lambdaQuery().eq(RecordCourseQuestion::getCourseId, courseId));
        LambdaUpdateWrapper<RecordCourseQuestion> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        if (questionType == Question.questionTypeEnum.singleChoiceQuestion.getCode()){
            // 单选题
            lambdaUpdateWrapper.eq(RecordCourseQuestion::getCourseId,courseId).set(RecordCourseQuestion::getSingleChoiceQuestionsCount,recordCourseQuestion.getSingleChoiceQuestionsCount()-1);
        }
        if (questionType == Question.questionTypeEnum.multipleChoiceQuestion.getCode()){
            // 多选题
            lambdaUpdateWrapper.eq(RecordCourseQuestion::getCourseId,courseId).set(RecordCourseQuestion::getMultipleChoiceQuestionsCount,recordCourseQuestion.getMultipleChoiceQuestionsCount()-1);
        }
        if (questionType == Question.questionTypeEnum.fillTheBlanksQuestion.getCode()){
            // 填空题
            lambdaUpdateWrapper.eq(RecordCourseQuestion::getCourseId,courseId).set(RecordCourseQuestion::getFillTheBlanksQuestionsCount,recordCourseQuestion.getSingleChoiceQuestionsCount()-1);
        }
        if (questionType == Question.questionTypeEnum.answerQuestion.getCode()){
            // 解答题
            lambdaUpdateWrapper.eq(RecordCourseQuestion::getCourseId,courseId).set(RecordCourseQuestion::getAnswerQuestionsCount,recordCourseQuestion.getSingleChoiceQuestionsCount()-1);
        }
        recordCourseQuestionMapper.update(null,lambdaUpdateWrapper);
        questionMapper.deleteById(questionId);
        questionTagMapper.delete(Wrappers.<QuestionTag>lambdaQuery().eq(QuestionTag::getQuestionId,questionId));
        questionItemMapper.delete(Wrappers.<QuestionItem>lambdaQuery().eq(QuestionItem::getQuestionId,questionId));
        return ResponseResult.okResult();
    }
}
