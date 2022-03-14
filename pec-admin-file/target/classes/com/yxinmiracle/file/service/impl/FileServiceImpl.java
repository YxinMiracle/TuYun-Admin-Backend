package com.yxinmiracle.file.service.impl;

import com.yxinmiracle.file.service.FileService;
import com.yxinmiracle.model.common.dtos.ResponseResult;
import com.yxinmiracle.model.common.enums.AppHttpCodeEnum;
import com.yxinmiracle.utils.common.FileUtil;
import com.yxinmiracle.utils.common.TxFileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Objects;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {
    Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    @Value("${host}")
    private String HOST;


    @Override
    public ResponseResult upload(MultipartFile image) {
        try {
            if (!Objects.isNull(image)){
                String originalFilename = image.getOriginalFilename(); // 上传的图片名字
                String extName = StringUtils.getFilenameExtension(originalFilename);  // 图片的后缀名
                String fileName = UUID.randomUUID().toString().replaceAll("-", "") + "." + extName;
                File file = null;
                file = FileUtil.multipartFileToFile(image,fileName);
                String picPath = TxFileUtil.upload(file,fileName);
                logger.debug(HOST+picPath);
                return ResponseResult.okResult(picPath,HOST);
            }
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        } catch (Exception e) {
            throw new RuntimeException("图片上传出现错误");
        }
    }
}
