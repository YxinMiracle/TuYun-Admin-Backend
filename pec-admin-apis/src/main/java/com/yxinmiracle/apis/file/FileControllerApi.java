package com.yxinmiracle.apis.file;

import com.yxinmiracle.model.common.dtos.ResponseResult;
<<<<<<< HEAD
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
=======
>>>>>>> parent of 92a6c76 (add file fuction)
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

public interface FileControllerApi {
    public ResponseResult upload(MultipartFile image);

    /**
     * 文件上传前的准备工作，校验文件是否存在
     * @param fileMd5
     * @param fileName
     * @param fileSize
     * @param mimetype
     * @param fileExt
     * @return
     */
    @ApiOperation("视频上传注册")
    public ResponseResult register(String fileMd5,String fileName,Long fileSize,String mimetype,String fileExt);

    /**
     * 校验分块文件是否存在
     * @param fileMd5
     * @param chunk
     * @param chunkSize
     * @return
     */
    @ApiOperation("校验分块文件是否存在")
    public ResponseResult checkChunk(String fileMd5,Integer chunk,Integer chunkSize);

    @ApiOperation("上传分块")
    public ResponseResult uploadChunk(MultipartFile file, String fileMd5, Integer chunk);

    @ApiOperation("合并分块")
    public ResponseResult mergeChunks(String fileMd5,String fileName,Long fileSize,String mimetype,String fileExt, String videoPart, String courseId,String videoName);

    @ApiOperation("获得视频资源")
    public ResponseResult getVideoByCourseIdAndVideoPart(String  courseId,String  videoPart);

    @ApiOperation("通过某一个课程的id获取该课程的视频列表")
    public ResponseResult getVideoListByCourseId(String courseId);
}
