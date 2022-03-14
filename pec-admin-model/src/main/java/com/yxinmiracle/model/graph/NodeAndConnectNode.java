package com.yxinmiracle.model.graph;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class NodeAndConnectNode implements Serializable {
    private String nodeLabelName;
    private String connectNodeLabelName;
}
