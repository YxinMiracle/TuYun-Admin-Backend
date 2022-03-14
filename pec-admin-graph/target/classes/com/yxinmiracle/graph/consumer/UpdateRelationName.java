package com.yxinmiracle.graph.consumer;


import com.yxinmiracle.graph.utils.GraphUtils;
import com.yxinmiracle.model.graph.AddNodeAndRelationDto;
import com.yxinmiracle.model.serives.dtos.RelationDto;
import com.yxinmiracle.model.serives.pojos.Relation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RabbitListener(queues = "graph.update.relationName")
public class UpdateRelationName {
    Logger logger = LoggerFactory.getLogger(UpdateRelationName.class);

    @RabbitHandler
    public void reviveMessage(RelationDto dto){
        logger.info("接收到更新relationName的数据{}",dto);
        List<Relation> relationDataList = dto.getRelationDataList();
        for (Relation relation : relationDataList) {
            GraphUtils.updateRelationName(relation,dto.getCourseName());
        }
    }

}
