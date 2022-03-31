package com.yxinmiracle.file.service;

import com.yxinmiracle.model.common.dtos.ResponseResult;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    public ResponseResult upload(MultipartFile image);

    ResponseResult register(String fileMd5, String fileName, Long fileSize, String mimetype, String fileExt);

    ResponseResult checkChunk(String fileMd5, Integer chunk, Integer chunkSize);

    ResponseResult uploadChunk(MultipartFile file, String fileMd5, Integer chunk);

    ResponseResult mergeChunks(String fileMd5, String fileName, Long fileSize, String mimetype, String fileExt, String videoPart, String courseId,String videoName);

    ResponseResult getVideoByCourseIdAndVideoPart(String courseId, String videoPart);

    ResponseResult getVideoListByCourseId(String courseId);
}
