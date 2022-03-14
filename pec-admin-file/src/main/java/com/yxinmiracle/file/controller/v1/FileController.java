package com.yxinmiracle.file.controller.v1;

import com.yxinmiracle.apis.file.FileControllerApi;
import com.yxinmiracle.file.service.FileService;
import com.yxinmiracle.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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



}
