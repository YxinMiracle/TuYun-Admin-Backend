package com.yxinmiracle.course.controller.v1;

import com.yxinmiracle.apis.services.ServicesControllerApi;
import com.yxinmiracle.course.service.ServicesService;
import com.yxinmiracle.utils.common.JsoupUtil;
import com.yxinmiracle.model.common.dtos.ResponseResult;
import com.yxinmiracle.model.serives.dtos.ServicesDto;
import com.yxinmiracle.model.serives.pojos.Services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/api/v1/services")
@RestController
public class ServicesController implements ServicesControllerApi {

    @Autowired
    private ServicesService servicesService;

    @Override
    @PostMapping
    @PreAuthorize("hasAuthority('system:services:query')")
    public ResponseResult getServices(@RequestBody ServicesDto dto) {
        return servicesService.getServices(dto);
    }

    @Override
    @RequestMapping(value = "/handle",method = RequestMethod.POST)
    public ResponseResult addServices(@RequestBody Services services) {
        return servicesService.addServices(services);
    }

    @Override
    @RequestMapping(value = "/handle",method = RequestMethod.PUT)
    public ResponseResult updateServices(@RequestBody Services services) { // 更新操作
        return servicesService.updateServices(services);
    }


    @Override
    @RequestMapping(value = "/handle/{servicesId}" ,method = RequestMethod.DELETE)
    public ResponseResult deleteServices(@PathVariable("servicesId") Integer servicesId) {
        return servicesService.deleteServices(servicesId);
    }

    @Override
    @PostMapping(value = "/group/all")
    public ResponseResult getGroupServices() {
        return servicesService.getGroupServices();
    }

}
