package com.yxinmiracle.graph.controller.v1;

import com.yxinmiracle.graph.service.GraphService;
import com.yxinmiracle.model.common.dtos.ResponseResult;
import com.yxinmiracle.model.serives.pojos.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/graph")
public class GraphController {

    @Autowired
    private GraphService graphService;

    @GetMapping("/config")
    public ResponseResult<List<Course>> getConfig(){
        return graphService.getConfig();
    }

    @GetMapping("/cypher/{courseName}")
    private ResponseResult getCypherByCourseName(@PathVariable("courseName") String courseName){
        return graphService.getCypherByCourseName(courseName);
    }
}
