package com.yxinmiracle.model.graph;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class ConnectNode implements Serializable {
    private String label;
    private String knowledgePointStartName;
    private String relationName;
    private String knowledgePointEndName;
    private String courseName;
}
