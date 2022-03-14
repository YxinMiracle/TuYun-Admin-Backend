package com.yxinmiracle.graph.consumer;


import com.yxinmiracle.graph.utils.GraphUtils;
import com.yxinmiracle.model.serives.dtos.CourseTagAndTagPropertyDto;
import com.yxinmiracle.model.serives.dtos.RelationDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "graph.update.tagProperty")
public class UpdateTagProperty {
    Logger logger = LoggerFactory.getLogger(UpdateTagProperty.class);

    @RabbitHandler
    public void reviveMessage(CourseTagAndTagPropertyDto dto){
        GraphUtils.updateNodeNameAndAddProperty(dto);
    }
}

