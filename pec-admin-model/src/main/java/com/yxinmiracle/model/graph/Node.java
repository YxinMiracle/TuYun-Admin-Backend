package com.yxinmiracle.model.graph;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Node implements Serializable {
    private String label;
    private String nodeName;
    private String desc;
    private String courseName;
    private short isDelete;
    private Integer courseId;
    private Integer courseTagId;

    public enum isDeleteEnum {
        isDelete((short) 1), notDelete((short) 0);

        @Getter
        short code;

        isDeleteEnum(short code) {
            this.code = code;
        }
    }
}
