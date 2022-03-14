package com.yxinmiracle.apis.user.feign;

import com.yxinmiracle.model.common.dtos.ResponseResult;
import com.yxinmiracle.model.serives.pojos.Services;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "pec-admin-user",path = "/synchronization/services")
public interface ServicesFeign {


    @RequestMapping(value = "/handle",method = RequestMethod.POST)
    public ResponseResult addServices(@RequestBody Services services);

    @RequestMapping(value = "/handle",method = RequestMethod.PUT)
    public ResponseResult updateServices(@RequestBody Services services);

    @RequestMapping(value = "/handle/{servicesId}" ,method = RequestMethod.DELETE)
    public ResponseResult deleteServices(@PathVariable("servicesId") Integer servicesId);


}
