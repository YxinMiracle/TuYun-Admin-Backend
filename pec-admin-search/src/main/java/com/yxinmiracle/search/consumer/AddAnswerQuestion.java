package com.yxinmiracle.search.consumer;

import com.alibaba.fastjson.JSON;
import com.yxinmiracle.apis.services.feign.CourseTagFeign;
import com.yxinmiracle.common.search.EsCourseCommon;
import com.yxinmiracle.model.search.es.EsQuestion;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@RabbitListener(queues = "search.add.answerQuestion")
public class AddAnswerQuestion {
    Logger logger = LoggerFactory.getLogger(AddAnswerQuestion.class);

    @Autowired
    public CourseTagFeign courseTagFeign;

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    /**
     * 添加解答题
     * @param esQuestion
     */
    @RabbitHandler
    public void addChoiceQuestion(EsQuestion esQuestion) throws IOException {
        logger.info("Es Rabbit Mq 接收到信息:{}",esQuestion);
        logger.info("开始添加解答题进入Es中");
        // 处理数据
        List<String> courseTagName = courseTagFeign.getCourseTagByCourseTagIds(esQuestion.getQuestionTagIds());
        esQuestion.setQuestionTagList(courseTagName);
        esQuestion.getQuestion().setCreateQuestionUserName("");
        esQuestion.getQuestion().setCreateQuestionUserId(-1);

        // 创建请求
        IndexRequest request = new IndexRequest(EsCourseCommon.COURSE_DOC_KEY);
        // 创建规则
        request.timeout(TimeValue.timeValueSeconds(2));
        // 放入数据
        request.source(JSON.toJSONString(esQuestion), XContentType.JSON);
        IndexResponse response = restHighLevelClient.index(request, RequestOptions.DEFAULT);
        if (response.status().equals("CREATED")){
            logger.info("插入数据成功");
        }else {
            logger.info("插入数据出现问题");
        }
    }
}
