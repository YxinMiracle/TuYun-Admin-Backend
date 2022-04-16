package com.yxinmiracle.search.consumer;

import com.alibaba.fastjson.JSON;
import com.yxinmiracle.common.search.EsCourseCommon;
import com.yxinmiracle.model.search.es.EsQuestion;
import com.yxinmiracle.model.search.es.EsQuestionTag;
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
@RabbitListener(queues = "search.add.questionTag")
public class AddQuestionTag {
    Logger logger = LoggerFactory.getLogger(AddQuestionTag.class);

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @RabbitHandler
    public void addQuestionTag(List<String> questionTags) throws IOException {
        logger.info("Es  addQuestionTag Mq 接收到信息:{}",questionTags);
        logger.info("开始添加选择题进入Es中");
        // 处理数据
        for (String questionTag : questionTags) {
            EsQuestionTag esQuestionTag = new EsQuestionTag();
            esQuestionTag.setQuestionTagName(questionTag);
            // 创建请求
            IndexRequest request = new IndexRequest(EsCourseCommon.QUESTION_TAG_DOC_KEY);
            // 创建规则
            request.timeout(TimeValue.timeValueSeconds(2));
            // 放入数据
            request.source(JSON.toJSONString(esQuestionTag), XContentType.JSON);
            IndexResponse response = restHighLevelClient.index(request, RequestOptions.DEFAULT);
        }

    }
}
