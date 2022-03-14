package com.yxinmiracle.advert.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yxinmiracle.model.advert.pojos.CategoryItem;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CategoryMapper  extends BaseMapper<CategoryItem> {

}
