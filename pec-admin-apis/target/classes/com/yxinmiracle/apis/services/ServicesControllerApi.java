package com.yxinmiracle.apis.services;

import com.yxinmiracle.model.common.dtos.ResponseResult;
import com.yxinmiracle.model.serives.dtos.ServicesDto;
import com.yxinmiracle.model.serives.pojos.Services;

public interface ServicesControllerApi {

    public ResponseResult getServices(ServicesDto dto);

    public ResponseResult addServices(Services services);

    public ResponseResult updateServices(Services services);

    public ResponseResult deleteServices(Integer servicesId);

    public ResponseResult getGroupServices();
}
