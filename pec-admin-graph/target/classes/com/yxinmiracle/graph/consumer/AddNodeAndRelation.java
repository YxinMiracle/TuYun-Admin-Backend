package com.yxinmiracle.graph.consumer;

import com.yxinmiracle.graph.utils.GraphUtils;
import com.yxinmiracle.model.graph.AddNodeAndRelationDto;
import com.yxinmiracle.model.graph.ConnectNode;
import com.yxinmiracle.model.graph.Node;
import com.yxinmiracle.model.graph.NodeAndConnectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RabbitListener(queues = "graph.add.relationAndNode")
public class AddNodeAndRelation {
    Logger logger = LoggerFactory.getLogger(AddNodeAndRelation.class);

    @RabbitHandler
    public void reviveMessage(AddNodeAndRelationDto dto){
        logger.info("RabbitMq:队列为 ===> graph.add.relationAndNode 的数据为:{}",dto);
        List<ConnectNode> connectNodeList = dto.getConnectNode();
        for (ConnectNode connectNode : connectNodeList) {
            GraphUtils.createRelationNode(connectNode);
        }
        List<Node> nodeList = dto.getNode();
        for (Node node : nodeList) {
            GraphUtils.createNode(node);
        }
        NodeAndConnectNode nodeAndConnectNode = dto.getNodeAndConnectNode();
        GraphUtils.addRealRelation(nodeAndConnectNode);
        logger.info("添加图数据库数据成功");
    }

}
