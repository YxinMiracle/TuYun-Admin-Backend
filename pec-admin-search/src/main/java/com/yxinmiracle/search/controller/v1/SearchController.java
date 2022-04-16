package com.yxinmiracle.search.controller.v1;

import com.yxinmiracle.apis.search.SearchControllerApi;
import com.yxinmiracle.model.common.dtos.ResponseResult;
import com.yxinmiracle.model.search.dto.SearchDto;
import com.yxinmiracle.search.service.SearchService;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
@RequestMapping("/api/v1/search")
public class SearchController implements SearchControllerApi {
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    private SearchService searchService;

    @GetMapping
    public String test(){
        CreateIndexRequest request = new CreateIndexRequest("yx-test");
        CreateIndexResponse createIndexResponse = null;
        try {
            createIndexResponse = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(createIndexResponse);
        return "ok";
    }

    @Override
    @PostMapping("/s")
    public ResponseResult searchPage(@RequestBody SearchDto dto) throws IOException {
        return searchService.searchPage(dto);
    }

    @GetMapping("/sugrec/{pwd}")
    @Override
    public ResponseResult sugrec(@PathVariable("pwd") String pwd) throws IOException {
        return searchService.sugrec(pwd);
    }
}
