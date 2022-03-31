package com.yxinmiracle.file.controller.v1;

import com.yxinmiracle.apis.file.FileControllerApi;
import com.yxinmiracle.file.service.FileService;
import com.yxinmiracle.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/v1/file")
public class FileController implements FileControllerApi {

    @Autowired
    private FileService fileService;

    @PostMapping("/upload/image")
    public ResponseResult upload(@RequestParam("image") MultipartFile image) {
        return fileService.upload(image);
    }

    @PostMapping("/register")
    @CrossOrigin
    @Override
    public ResponseResult register(String fileMd5, String fileName, Long fileSize, String mimetype, String fileExt) {
        return fileService.register(fileMd5, fileName, fileSize, mimetype, fileExt);
    }

    @PostMapping("/checkchunk")
    @CrossOrigin
    @Override
    public ResponseResult checkChunk(String fileMd5, Integer chunk, Integer chunkSize) {
        return fileService.checkChunk(fileMd5, chunk, chunkSize);
    }

    @PostMapping("/uploadchunk")
    @CrossOrigin
    @Override
    public ResponseResult uploadChunk(MultipartFile file, String fileMd5, Integer chunk) {
        return fileService.uploadChunk(file, fileMd5, chunk);
    }

    @PostMapping("/mergechunks")
    @CrossOrigin
    @Override
    public ResponseResult mergeChunks(String fileMd5, String fileName, Long fileSize, String mimetype, String fileExt, String videoPart, String courseId,String videoName) {
        return fileService.mergeChunks(fileMd5, fileName, fileSize, mimetype, fileExt, videoPart, courseId,videoName);
    }

    @Override
    @GetMapping("/get/video/{courseId}/{videoPart}")
    public ResponseResult getVideoByCourseIdAndVideoPart(@PathVariable("courseId") String courseId, @PathVariable("videoPart") String videoPart) {
        return fileService.getVideoByCourseIdAndVideoPart(courseId, videoPart);
    }

    /**
     * 通过courseId获取播放视频列表
     *
     * @param courseId
     * @return
     */
    @GetMapping("/video/list/{courseId}")
    @Override
    public ResponseResult getVideoListByCourseId(@PathVariable("courseId") String courseId) {
        return fileService.getVideoListByCourseId(courseId);
    }

    @Value("${pec-service-manage-media.upload-location}")
    private String uploadLocation;

    @GetMapping("/{filePathFirst}/{filePathSecond}/{fileId}/{fileFolder}/{fileName}")
    @CrossOrigin
    public ResponseEntity<Resource> downloadTs(@PathVariable("filePathFirst") String filePathFirst,
                                               @PathVariable("filePathSecond") String filePathSecond,
                                               @PathVariable("fileId") String fileId,
                                               @PathVariable("fileFolder") String fileFolder,
                                               @PathVariable("fileName") String fileName
    ) {
        String filePath = filePathFirst + "/" + filePathSecond + "/" + fileId + "/" + fileFolder + "/" + fileName;
        String contentDisposition = ContentDisposition
                .builder("attachment")
                .filename(uploadLocation + filePath)
                .build().toString();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .contentType(MediaType.IMAGE_JPEG)
                .body(new FileSystemResource(uploadLocation + filePath));
    }


}
