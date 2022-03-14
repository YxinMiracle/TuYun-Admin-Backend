package com.yxinmiracle.model.advert.pojos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


import java.io.Serializable;

@Data
@TableName("category_item")
public class CategoryItem implements Serializable {

    @TableId(value = "category_id",type = IdType.AUTO)
    private Integer categoryId;

    @TableField(value = "type_id")
    private Integer typeId;

    @TableField(value = "category_name")
    private String categoryName;

    @TableField(value = "category_herf")
    private String categoryHref;

    @TableField(value = "category_item_id")
    private Integer categoryItemId;

    @TableField(value = "category_sort")
    private Integer categorySort;

    @TableField(value = "image")
    private String image;



}
