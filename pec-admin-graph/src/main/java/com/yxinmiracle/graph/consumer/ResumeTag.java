package com.yxinmiracle.graph.consumer;

import com.yxinmiracle.graph.utils.GraphUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "graph.resume.tag")
public class ResumeTag {

    Logger logger = LoggerFactory.getLogger(ResumeTag.class);

    @RabbitHandler
    public void reviveMessage(Integer courseTagId){
        logger.info("正在恢复Node,tagId为{}",courseTagId);
        GraphUtils.resumeNode(courseTagId);
    }

}
