package com.yxinmiracle.search;

import com.alibaba.fastjson.JSON;
import com.yxinmiracle.apis.services.feign.CourseTagFeign;
import com.yxinmiracle.common.search.EsCourseCommon;
import com.yxinmiracle.model.search.es.EsQuestion;
import com.yxinmiracle.model.search.es.EsQuestionTag;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.MatchPhrasePrefixQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@RunWith(SpringRunner.class)
public class YxinEsTest {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    public CourseTagFeign courseTagFeign;

    /**
     * 在测试中先进行添加course文档
     * @throws IOException
     */
    @Test
    public void testCreateIndex() throws IOException {
        CreateIndexRequest request = new CreateIndexRequest("pec-course");
        CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
        System.out.println(createIndexResponse);
    }

    @Test
    public void testCreateIndex2() throws IOException {
        CreateIndexRequest request = new CreateIndexRequest("pec-question-tag");
        CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
        System.out.println(createIndexResponse);
    }

    @Test
    public void addCourseTag() throws IOException {
        List<String> quesitonTag3 = new ArrayList<>();
        quesitonTag3.add("多线程");
        quesitonTag3.add("多进程");
        quesitonTag3.add("爬虫");
        quesitonTag3.add("python");
        quesitonTag3.add("函数");
        quesitonTag3.add("类");
        quesitonTag3.add("C语言");
        quesitonTag3.add("异步的东西");
        quesitonTag3.add("程序流程图");
        quesitonTag3.add("UML");
        quesitonTag3.add("系统架构图");
        quesitonTag3.add("系统分析");
        quesitonTag3.add("UML系统建模及程序分析与设计");
        quesitonTag3.add("UML课");
        quesitonTag3.add("UML的书");
        quesitonTag3.add("面向对象");
        quesitonTag3.add("Java");
        quesitonTag3.add("Maven");
        quesitonTag3.add("类");
        quesitonTag3.add("Springboot");
        quesitonTag3.add("面向过程");
        quesitonTag3.add("分布式");
        quesitonTag3.add("JavaWeb");
        quesitonTag3.add("Python");
        for (String s : quesitonTag3) {
            EsQuestionTag esQuestionTag = new EsQuestionTag();
            esQuestionTag.setQuestionTagName(s);
            // 创建请求
            IndexRequest request = new IndexRequest(EsCourseCommon.QUESTION_TAG_DOC_KEY);
            // 创建规则
            request.timeout(TimeValue.timeValueSeconds(2));
            // 放入数据
            request.source(JSON.toJSONString(esQuestionTag), XContentType.JSON);
            IndexResponse response = restHighLevelClient.index(request, RequestOptions.DEFAULT);
        }

    }

    @Test
    public void testPrefix() throws IOException {
        SearchRequest searchRequest = new SearchRequest(EsCourseCommon.QUESTION_TAG_DOC_KEY);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        // 匹配
//        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(dto.getKeyWord(), "question.subject", "question.analysis");
        MatchPhrasePrefixQueryBuilder matchPhrasePrefixQueryBuilder = QueryBuilders.matchPhrasePrefixQuery("questionTagName", "多");
        sourceBuilder.query(matchPhrasePrefixQueryBuilder);
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));



        // 执行
        searchRequest.source(sourceBuilder);
        SearchResponse search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        for (SearchHit hit : search.getHits().getHits()) {
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            System.out.println(sourceAsMap);
        }
    }


    @Test
    public void testFeign(){
        List<String> ids = new ArrayList<>();
        ids.add("分布式");
        ids.add("Java");
        System.out.println(JSON.toJSONString(ids));


    }


}
