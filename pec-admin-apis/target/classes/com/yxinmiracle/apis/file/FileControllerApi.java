package com.yxinmiracle.apis.file;

import com.yxinmiracle.model.common.dtos.ResponseResult;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

public interface FileControllerApi {
    public ResponseResult upload(MultipartFile image) ;
}
