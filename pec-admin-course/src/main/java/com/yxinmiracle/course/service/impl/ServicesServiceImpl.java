package com.yxinmiracle.course.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yxinmiracle.apis.user.feign.ServicesFeign;
import com.yxinmiracle.common.course.ServicesCommom;
import com.yxinmiracle.course.mapper.ServicesMapper;
import com.yxinmiracle.course.service.ServicesService;
import com.yxinmiracle.model.common.dtos.PageResponseResult;
import com.yxinmiracle.model.common.dtos.ResponseResult;
import com.yxinmiracle.model.common.enums.AppHttpCodeEnum;
import com.yxinmiracle.model.serives.dtos.ServicesDto;
import com.yxinmiracle.model.serives.pojos.Services;
import com.yxinmiracle.utils.common.JsoupUtil;
import com.yxinmiracle.utils.common.PinyinUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ServicesServiceImpl implements ServicesService {
    @Autowired
    private ServicesMapper servicesMapper;

    @Value("${host}")
    private String HOST;

    @Autowired
    private ServicesFeign servicesFeign;

    @Override
    public ResponseResult getServices(ServicesDto dto) {
        if (Objects.isNull(dto)){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        dto.checkParam();
        IPage<Services> page = new Page<Services>(dto.getPage(),dto.getSize());
        LambdaQueryWrapper<Services> lambdaQueryWrapper = new LambdaQueryWrapper();

        if (StringUtils.isNotBlank(dto.getServicesName())){
            lambdaQueryWrapper.like(Services::getServicesName,dto.getServicesName());
        }

        if (StringUtils.isNotBlank(dto.getLetter())){
            lambdaQueryWrapper.like(Services::getLetter,dto.getLetter());
        }

        lambdaQueryWrapper.eq(Services::getIsDelete,Services.isDeleteType.notDelete.getCode());

        IPage<Services> servicesIPage = servicesMapper.selectPage(page, lambdaQueryWrapper);

        //3.结果返回
        ResponseResult responseResult = new PageResponseResult(dto.getPage(),dto.getSize(),(int)servicesIPage.getTotal());
        responseResult.setData(servicesIPage.getRecords());
        responseResult.setErrorMessage("请求成功");
        responseResult.setCode(AppHttpCodeEnum.SUCCESS.getCode());
        responseResult.setHost(HOST);
        return responseResult;
    }

    @Override
    @Transactional
    public ResponseResult addServices(Services services) {
        servicesFeign.addServices(services);
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

    @Override
    @Transactional
    public ResponseResult updateServices(Services services) {
        servicesFeign.updateServices(services);
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

    @Override
    @Transactional
    public ResponseResult deleteServices(Integer servicesId) {
        servicesFeign.deleteServices(servicesId);
        if (servicesId==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        LambdaUpdateWrapper<Services> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(Services::getServicesId, servicesId).set(Services::getIsDelete, Services.isDeleteType.isDelete.getCode());
        servicesMapper.update(null, lambdaUpdateWrapper);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getGroupServices() {
        List<Services> servicesList = servicesMapper.selectList(null);
        Map<String,List<Services>> resultMap = new HashMap<>();
        List<Map<String, Object>> returnList = new ArrayList<>();
        servicesList.forEach(services -> {
            if (resultMap.containsKey(services.getLetter()+"")){
                List<Services> list = resultMap.get(services.getLetter()+"");
                list.add(services);
                resultMap.put(services.getLetter()+"",list);
            }else { // 如果不存在
                resultMap.put(services.getLetter()+"",new ArrayList<>(Arrays.asList(services)));
            }
        });
        for (Map.Entry<String, List<Services>> stringListEntry : resultMap.entrySet()) {
            String key = stringListEntry.getKey();
            List<Services> value = stringListEntry.getValue();
            Map<String, Object> map = new HashMap<>();
            map.put("key", key);
            map.put("value", value);
            returnList.add(map);
        }
        List<Map<String, Object>> collect = returnList.stream().sorted((x, y) -> {
            return (int) (x.get("key") + "").toCharArray()[0] - (int) (y.get("key") + "").toCharArray()[0];
        }).collect(Collectors.toList());
        return ResponseResult.okResult(collect);
    }
}
