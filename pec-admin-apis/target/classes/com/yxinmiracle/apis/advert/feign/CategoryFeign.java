package com.yxinmiracle.apis.advert.feign;

import com.yxinmiracle.model.common.dtos.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "pec-admin-advert",path = "/api/v1/advert")
public interface CategoryFeign {
    @PostMapping("/list")
    public ResponseResult getCategoryList();

}
