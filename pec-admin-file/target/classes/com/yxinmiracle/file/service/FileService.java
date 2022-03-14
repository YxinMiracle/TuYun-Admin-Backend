package com.yxinmiracle.file.service;

import com.yxinmiracle.model.common.dtos.ResponseResult;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    public ResponseResult upload(MultipartFile image);
}
