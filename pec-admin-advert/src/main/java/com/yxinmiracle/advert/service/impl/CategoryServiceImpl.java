package com.yxinmiracle.advert.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yxinmiracle.advert.mapper.CategoryMapper;
import com.yxinmiracle.advert.service.CategoryService;
import com.yxinmiracle.model.advert.pojos.CategoryItem;
import com.yxinmiracle.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public ResponseResult getCategoryList() {
        List<CategoryItem> categoryItems = categoryMapper.selectList(Wrappers.<CategoryItem>lambdaQuery().isNull(CategoryItem::getCategoryItemId));
        return ResponseResult.okResult(categoryItems);
    }
}
