package com.yxinmiracle.model.serives.vos;

import com.yxinmiracle.model.advert.pojos.CategoryItem;
import com.yxinmiracle.model.serives.pojos.Course;
import com.yxinmiracle.model.serives.pojos.Services;
import com.yxinmiracle.model.user.pojos.User;
import lombok.Data;


import java.io.Serializable;
import java.util.List;

@Data
public class CourseVo extends Course implements Serializable {
    private Services services;
    private CategoryItem categoryItem;
    private List<User> users;
}
