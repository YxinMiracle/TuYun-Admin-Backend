package com.yxinmiracle.model.serives.vos;

import lombok.Data;

import java.io.Serializable;

@Data
public class CourseVideoVo implements Serializable {
    private String filePath;
    private String fileName;
    private String fileOriginalName;
    private String fileUrl;
    private String videoPart;
    private String videoTime;
    private String videoName;
}
