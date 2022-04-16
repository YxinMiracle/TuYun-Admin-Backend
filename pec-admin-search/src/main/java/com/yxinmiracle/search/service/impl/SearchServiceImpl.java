package com.yxinmiracle.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.yxinmiracle.common.search.EsCourseCommon;
import com.yxinmiracle.model.common.dtos.ResponseResult;
import com.yxinmiracle.model.common.enums.AppHttpCodeEnum;
import com.yxinmiracle.model.search.dto.SearchDto;
import com.yxinmiracle.model.search.es.EsQuestion;
import com.yxinmiracle.search.service.SearchService;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.MatchPhrasePrefixQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Override
    public ResponseResult searchPage(SearchDto dto) throws IOException {
        if (Objects.isNull(dto)){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        dto.checkParam();
        SearchRequest searchRequest = new SearchRequest(EsCourseCommon.COURSE_DOC_KEY);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        // 分页
        sourceBuilder.from((dto.getPage()-1)*dto.getSize());
        sourceBuilder.size(dto.getSize());

        // 匹配
        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(dto.getKeyWord(), "question.subject", "question.analysis");
        sourceBuilder.query(multiMatchQueryBuilder);
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

        // 高亮构建
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("question.subject");
        highlightBuilder.requireFieldMatch(false);
        highlightBuilder.preTags("<span style='color:red'>");
        highlightBuilder.postTags("</span>");
        sourceBuilder.highlighter(highlightBuilder);

        // 执行
        searchRequest.source(sourceBuilder);
        SearchResponse search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        // 解析
        for (SearchHit hit : search.getHits().getHits()) {
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            HighlightField highlightField = highlightFields.get("question.subject");
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            if (highlightField!=null){
                Text[] fragments = highlightField.fragments();
                String newSubject = "";
                for (Text text : fragments) {
                    newSubject += text;
                }
                EsQuestion esQuestion = JSON.parseObject(JSON.toJSONString(sourceAsMap), EsQuestion.class);
                esQuestion.getQuestion().setSubject(newSubject);
                System.out.println(esQuestion);
            }
//            System.out.println(documentFields.getSourceAsMap());
        }
        return null;
    }

    @Override
    public ResponseResult sugrec(String pwd) throws IOException {
        SearchRequest searchRequest = new SearchRequest(EsCourseCommon.QUESTION_TAG_DOC_KEY);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        // 匹配
        MatchPhrasePrefixQueryBuilder matchPhrasePrefixQueryBuilder = QueryBuilders.matchPhrasePrefixQuery("questionTagName", pwd);
        sourceBuilder.query(matchPhrasePrefixQueryBuilder);
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

        // 执行
        searchRequest.source(sourceBuilder);
        SearchResponse search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

        // 结果
        List<String> resultQuestionTagNameList = new ArrayList<>();
        for (SearchHit hit : search.getHits().getHits()) {
            Map<String, Object> resultMap = hit.getSourceAsMap();
            resultQuestionTagNameList.add((String) resultMap.get("questionTagName"));
        }
        return ResponseResult.okResult(resultQuestionTagNameList);
    }
}
