package com.yxinmiracle.advert.controller.v1;

import com.yxinmiracle.advert.AdvertApplication;
import com.yxinmiracle.advert.service.CategoryService;
import com.yxinmiracle.apis.advert.CategoryControllerApi;
import com.yxinmiracle.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/category")
public class CategoryController implements CategoryControllerApi {

    @Autowired
    private CategoryService categoryService;

    @Override
    @GetMapping("/list")
    public ResponseResult getCategoryList() {
        return categoryService.getCategoryList();
    }
}
