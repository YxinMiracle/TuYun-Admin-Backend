package com.yxinmiracle.graph.consumer;

import com.yxinmiracle.graph.utils.GraphUtils;
import com.yxinmiracle.model.serives.dtos.RelationDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "graph.delete.tag")
public class DeleteTag {
    Logger logger = LoggerFactory.getLogger(DeleteTag.class);

    @RabbitHandler
    public void reviveMessage(Integer courseTagId){
        logger.info("正在删除node,tagId为{}",courseTagId);
        GraphUtils.deleteNode(courseTagId);
    }
}
