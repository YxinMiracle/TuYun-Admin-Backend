package com.yxinmiracle.course.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yxinmiracle.course.mapper.CourseVideoCountMapper;
import com.yxinmiracle.course.mapper.CourseVideoMapper;
import com.yxinmiracle.course.service.CourseVideoService;
import com.yxinmiracle.model.common.dtos.PageResponseResult;
import com.yxinmiracle.model.common.dtos.ResponseResult;
import com.yxinmiracle.model.common.enums.AppHttpCodeEnum;
import com.yxinmiracle.model.serives.dtos.CourseVideoCountDto;
import com.yxinmiracle.model.serives.dtos.CourseVideoDto;
import com.yxinmiracle.model.serives.pojos.CourseVideo;
import com.yxinmiracle.model.serives.pojos.CourseVideoCount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CourseVideoServiceImpl implements CourseVideoService {
    @Autowired
    private CourseVideoCountMapper courseVideoCountMapper;

    @Value("${host}")
    private String HOST;

    @Override
    public ResponseResult getCourseVideoCountData(CourseVideoCountDto dto) {

        IPage page = new Page(dto.getPage(),dto.getSize());
        LambdaQueryWrapper<CourseVideoCount> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        IPage resultPage = courseVideoCountMapper.selectPage(page, lambdaQueryWrapper);
        ResponseResult responseResult = new PageResponseResult(dto.getPage(),dto.getSize(), (int) resultPage.getTotal());
        responseResult.setHost(HOST);
        responseResult.setErrorMessage(AppHttpCodeEnum.SUCCESS.getErrorMessage());
        responseResult.setCode(AppHttpCodeEnum.SUCCESS.getCode());
        responseResult.setData(resultPage.getRecords());
        return responseResult;
    }
}
