package com.yxinmiracle.user.controller.v1;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.yxinmiracle.model.common.dtos.ResponseResult;
import com.yxinmiracle.model.common.enums.AppHttpCodeEnum;
import com.yxinmiracle.model.serives.pojos.Services;
import com.yxinmiracle.user.mapper.ServicesMapper;
import com.yxinmiracle.utils.common.JsoupUtil;
import com.yxinmiracle.utils.common.PinyinUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Objects;

@RestController
@RequestMapping("/synchronization/services")
public class ServicesController {

    @Autowired
    private ServicesMapper servicesMapper;

    @Value("${host}")
    private String HOST;

    @RequestMapping(value = "/handle",method = RequestMethod.POST)
    public ResponseResult addServices(@RequestBody Services services){
        if (Objects.isNull(services)){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        if (!services.check()){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        if (services.getImage()!=null && StringUtils.isNotBlank((services.getImage())) && services.getImage().startsWith(HOST)){
            services.setImage(services.getImage().startsWith(HOST)?services.getImage().replace(HOST,""):null);
        }

        services.setArticle(JsoupUtil.clean(services.getArticle())); // xss过滤
        services.setLetter(PinyinUtil.getPinYinHeadChar(services.getServicesName())); // 查询出机构名称的拼音首字母
        services.setCreateTime(new Date());
        services.setUpdateTime(new Date());
        services.setIsDelete(Services.isDeleteType.notDelete.getCode());

        servicesMapper.insert(services);
        return ResponseResult.okResult();
    }

    @RequestMapping(value = "/handle",method = RequestMethod.PUT)
    public ResponseResult updateServices(@RequestBody Services services){
        if (Objects.isNull(services)){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        if (!services.check()){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        if (services.getImage()!=null && StringUtils.isNotBlank((services.getImage())) && services.getImage().startsWith(HOST)){
            services.setImage(services.getImage().startsWith(HOST)?services.getImage().replace(HOST,""):null);
        }
        services.setArticle(JsoupUtil.clean(services.getArticle())); // xss过滤
        services.setLetter(PinyinUtil.getPinYinHeadChar(services.getServicesName())); // 查询出机构名称的拼音首字母

        services.setUpdateTime(new Date());
        servicesMapper.updateById(services);
        return ResponseResult.okResult();
    }

    @RequestMapping(value = "/handle/{servicesId}" ,method = RequestMethod.DELETE)
    public ResponseResult deleteServices(@PathVariable("servicesId") Integer servicesId){
        if (servicesId==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        LambdaUpdateWrapper<Services> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(Services::getServicesId, servicesId).set(Services::getIsDelete, Services.isDeleteType.isDelete.getCode());
        servicesMapper.update(null, lambdaUpdateWrapper);
        return ResponseResult.okResult();
    }





}
