package com.yxinmiracle.apis.search;

import com.yxinmiracle.model.common.dtos.ResponseResult;
import com.yxinmiracle.model.search.dto.SearchDto;

import java.io.IOException;

public interface SearchControllerApi {

    public ResponseResult searchPage(SearchDto dto) throws IOException;

    public ResponseResult sugrec(String pwd) throws IOException;
}
