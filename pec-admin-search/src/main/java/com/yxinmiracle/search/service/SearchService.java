package com.yxinmiracle.search.service;

import com.yxinmiracle.model.common.dtos.ResponseResult;
import com.yxinmiracle.model.search.dto.SearchDto;

import java.io.IOException;

public interface SearchService {
    public ResponseResult searchPage(SearchDto dto) throws IOException;

    ResponseResult sugrec(String pwd) throws IOException;
}
