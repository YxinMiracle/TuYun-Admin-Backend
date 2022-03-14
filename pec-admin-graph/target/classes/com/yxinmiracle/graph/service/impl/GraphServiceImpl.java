package com.yxinmiracle.graph.service.impl;

import com.yxinmiracle.apis.services.feign.CourseFeign;
import com.yxinmiracle.graph.service.GraphService;
import com.yxinmiracle.graph.utils.GraphUtils;
import com.yxinmiracle.model.common.dtos.ResponseResult;
import com.yxinmiracle.model.common.enums.AppHttpCodeEnum;
import com.yxinmiracle.model.serives.pojos.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GraphServiceImpl implements GraphService {

    @Autowired
    private CourseFeign courseFeign;

    @Override
    public ResponseResult getConfig() {
        Map returnMap = new HashMap();
        // label最外层的key都是课程的名称，也就是都在course表中
        Map labelsMap = new HashMap();
        // 构建relation的config
        Map relationshipsMap = new HashMap();
        ResponseResult<List<Course>> responseResult = courseFeign.getAllCourse();
        if (responseResult.getCode() != 200){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        List<Course> courseList = responseResult.getData();
        for (int i = 0; i < courseList.size(); i++) {
            String courseName = courseList.get(i).getCourseName();
            Map tempMap = new HashMap();
            tempMap.put("caption", "nodeName");
            tempMap.put("size", "pagerank");
            tempMap.put("community", "community");
            List<String> propertiesList = new ArrayList<>();
            propertiesList.add("courseName");
            propertiesList.add("nodeName");
            propertiesList.add("desc");
            tempMap.put("title_properties", propertiesList);
            labelsMap.put(courseName, tempMap);
            // 构建relationShipMap
            Map relationShipsCourseMap = new HashMap();
            relationShipsCourseMap.put("thickness", "weight");
            relationShipsCourseMap.put("caption", "relationName");
            relationshipsMap.put(courseName + "_relation", relationShipsCourseMap);
        }
        returnMap.put("labels", labelsMap);
        returnMap.put("relationships", relationshipsMap);
        return ResponseResult.okResult(returnMap);
    }

    @Override
    public ResponseResult getCypherByCourseName(String courseName) {
        String cypher = GraphUtils.getCourseAllGraphCypher(courseName);
        return ResponseResult.okResult(cypher);
    }
}

