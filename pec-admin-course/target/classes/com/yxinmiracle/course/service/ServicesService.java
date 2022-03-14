package com.yxinmiracle.course.service;


import com.yxinmiracle.model.common.dtos.ResponseResult;
import com.yxinmiracle.model.serives.dtos.ServicesDto;
import com.yxinmiracle.model.serives.pojos.Services;

public interface ServicesService {


    ResponseResult getServices(ServicesDto dto);

    ResponseResult addServices(Services services);

    ResponseResult updateServices(Services services);

    ResponseResult deleteServices(Integer servicesId);

    ResponseResult getGroupServices();

}
