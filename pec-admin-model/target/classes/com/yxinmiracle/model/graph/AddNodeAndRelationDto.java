package com.yxinmiracle.model.graph;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class AddNodeAndRelationDto implements Serializable {
    private List<Node> node;
    private List<ConnectNode> connectNode;
    private NodeAndConnectNode nodeAndConnectNode;
}
