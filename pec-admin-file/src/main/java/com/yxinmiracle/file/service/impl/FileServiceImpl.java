package com.yxinmiracle.file.service.impl;

import com.alibaba.fastjson.JSON;
import com.yxinmiracle.apis.services.feign.CourseVideoCountFeign;
import com.yxinmiracle.file.config.RabbitMQConfig;
import com.yxinmiracle.file.mapper.MediaFileRepository;
import com.yxinmiracle.file.service.FileService;
import com.yxinmiracle.model.common.dtos.ResponseResult;
import com.yxinmiracle.model.common.enums.AppHttpCodeEnum;
import com.yxinmiracle.model.file.pojos.MediaFile;
import com.yxinmiracle.model.serives.vos.CourseVideoVo;
import com.yxinmiracle.utils.common.FileUtil;
import com.yxinmiracle.utils.common.HlsVideoUtil;
import com.yxinmiracle.utils.common.TxFileUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FileServiceImpl implements FileService {
    Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    @Value("${host}")
    private String HOST;

    @Value("${pec-service-manage-media.upload-location}")
    private String uploadLocation;

    @Autowired
    private MediaFileRepository mediaFileRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Value("${mq.file.routingkey-media-video}")
    String routingkey_media_video;

    @Autowired
    private CourseVideoCountFeign courseVideoCountFeign;


    @Override
    public ResponseResult upload(MultipartFile image) {
        try {
            if (!Objects.isNull(image)) {
                String originalFilename = image.getOriginalFilename(); // 上传的图片名字
                String extName = StringUtils.getFilenameExtension(originalFilename);  // 图片的后缀名
                String fileName = UUID.randomUUID().toString().replaceAll("-", "") + "." + extName;
                File file = null;
                file = FileUtil.multipartFileToFile(image, fileName);
                String picPath = TxFileUtil.upload(file, fileName);
                logger.debug(HOST + picPath);
                return ResponseResult.okResult(picPath, HOST);
            }
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        } catch (Exception e) {
            throw new RuntimeException("图片上传出现错误");
        }
    }

    // 得到文件的所属目录
    private String getFileFolderPath(String fileMd5) {
        return uploadLocation + fileMd5.substring(0, 1) + "/" + fileMd5.substring(1, 2) + "/" + fileMd5 + "/";
    }

    private String getFilePath(String fileMd5, String fileExt) {
        return uploadLocation + fileMd5.substring(0, 1) + "/" + fileMd5.substring(1, 2) + "/" + fileMd5 + "/" + fileMd5 + "." + fileExt;
    }

    // 得到块文件所在的目录
    private String getChunkFileFolderPath(String fileMd5) {
        return uploadLocation + fileMd5.substring(0, 1) + "/" + fileMd5.substring(1, 2) + "/" + fileMd5 + "/chunk/";
    }

    // 文件上传前的注册
    @Override
    public ResponseResult register(String fileMd5, String fileName, Long fileSize, String mimetype, String fileExt) {
        String fileFolderPath = this.getFileFolderPath(fileMd5);
        String filePath = this.getFilePath(fileMd5, fileExt);
        File file = new File(filePath);
        boolean exists = file.exists();
        if (exists) {
            // 文件存在
            return ResponseResult.errorResult(500, "文件已经存在");
        }
        // 文件不存在的时候，做一些准备工作
        File fileFolder = new File(fileFolderPath);
        if (!fileFolder.exists()) {
            fileFolder.mkdirs();
        }
        return ResponseResult.okResult();
    }

    /**
     * @param fileMd5
     * @param chunk     块的下标
     * @param chunkSize 块的大小
     * @return
     */
    @Override
    public ResponseResult checkChunk(String fileMd5, Integer chunk, Integer chunkSize) {
        // 检查分块文件是否存在
        // 点得到分块文件的所在目录
        String chunkFileFolderPath = this.getChunkFileFolderPath(fileMd5);
        File chunkFile = new File(chunkFileFolderPath + chunk);
        if (chunkFile.exists()) {
            // 存在的话
            return ResponseResult.errorResult(500, "分块文件以及存在");
        } else {
            return ResponseResult.okResult();
        }
    }

    // 上传分块
    @Override
    public ResponseResult uploadChunk(MultipartFile file, String fileMd5, Integer chunk) {
        String chunkFileFolderPath = this.getChunkFileFolderPath(fileMd5);
        String chunkFilePath = chunkFileFolderPath + chunk;
        File chunkFileFolder = new File(chunkFileFolderPath);
        if (!chunkFileFolder.exists()) {
            chunkFileFolder.mkdirs();
        }
        InputStream inputStream = null;
        FileOutputStream outputStream = null;
        try {
            inputStream = file.getInputStream();
            outputStream = new FileOutputStream(new File(chunkFilePath));
            IOUtils.copy(inputStream, outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult mergeChunks(String fileMd5, String fileName, Long fileSize, String mimetype, String fileExt, String videoPart, String courseId,String videoName) {

        // 合并所有的分块
        String chunkFileFolderPath = this.getChunkFileFolderPath(fileMd5);

        File chunkFileFolder = new File(chunkFileFolderPath);
        // 分块文件列表、
        File[] files = chunkFileFolder.listFiles();
        List<File> fileList = Arrays.asList(files);

        //  创建合并后的文件
        String filePath = this.getFilePath(fileMd5, fileExt);
        File mergeFile = new File(filePath);
        mergeFile = this.mergeFile(fileList, mergeFile);
        if (mergeFile == null) {
            // 合并文件失败
            return ResponseResult.errorResult(500, "合并文件失败");
        }
        // 合并文件成功
        // 将文件信息写入数据库
        MediaFile mediaFile = new MediaFile();
        mediaFile.setFileId(fileMd5);
        mediaFile.setFileOriginalName(fileName);
        mediaFile.setFileName(fileMd5 + "." + fileExt);
        mediaFile.setFilePath(fileMd5.substring(0, 1) + "/" + fileMd5.substring(1, 2) + "/" + fileMd5 + "/");
        mediaFile.setFileSize(fileSize);
        mediaFile.setUploadTime(new Date());
        mediaFile.setFileType(fileExt);
        mediaFile.setMimeType(mimetype);
        mediaFile.setFileStatus("200");
        mediaFile.setVideoPart(videoPart);
        mediaFile.setCourseId(courseId);
        mediaFile.setVideoName(videoName);
        mediaFileRepository.save(mediaFile);

        // 通过feign调用添加回courseVideoCount表 count字段+1
        courseVideoCountFeign.updateCourseVideoCount(Integer.parseInt(courseId));
        // 校验文件的md5值是否和前端传入的md5一致
        //        boolean checkFileMd5 = this.checkFileMd5(mergeFile, fileMd5);
        //        if (!checkFileMd5){
        //            return ResponseResult.errorResult(500,"校验文件失败");
        //        }
        sendProcessVideoMsg(mediaFile.getFileId());// 发送视频处理消息
        return ResponseResult.okResult();
    }

    // 发送视频处理消息
    public ResponseResult sendProcessVideoMsg(String mediaId) {
        Optional<MediaFile> optional = mediaFileRepository.findById(mediaId);
        if (!optional.isPresent()) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        Map<String, String> map = new HashMap<>();
        map.put("mediaId", mediaId);
        String jsonString = JSON.toJSONString(map);
        try {
            rabbitTemplate.convertAndSend(RabbitMQConfig.EX_MEDIA_PROCESSTASK, routingkey_media_video, jsonString);
        } catch (AmqpException e) {
            e.printStackTrace();
            return ResponseResult.errorResult(AppHttpCodeEnum.VIDEO_SEND_MSG_ERROR);
        }
        return ResponseResult.okResult();
    }

    // 校验文件
    private boolean checkFileMd5(File mergeFile, String md5) {
        try {
            FileInputStream inputStream = new FileInputStream(mergeFile);
            String md5Hex = DigestUtils.md5Hex(inputStream);
            System.out.println(md5Hex);
            System.out.println(md5);
            if (md5.equalsIgnoreCase(md5Hex)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
        // 得到文件的md5
    }

    // 合并文件
    private File mergeFile(List<File> chunkFileList, File mergeFile) {
        try {
            // 如果合并文件已经存在则删除
            if (mergeFile.exists()) {
                mergeFile.delete();
            } else {
                // 创建一个新文件
                mergeFile.createNewFile();
            }
            Collections.sort(chunkFileList, new Comparator<File>() {
                @Override
                public int compare(File o1, File o2) {
                    if (Integer.parseInt(o1.getName()) > Integer.parseInt(o2.getName())) {
                        return 1;
                    }
                    return -1;
                }
            });
            // 创建一个写对象
            RandomAccessFile raf_write = new RandomAccessFile(mergeFile, "rw");
            byte[] b = new byte[1024];
            for (File file : chunkFileList) {
                RandomAccessFile raf_read = new RandomAccessFile(file, "r");
                int len = -1;
                while ((len = raf_read.read(b)) != -1) {
                    raf_write.write(b, 0, len);
                }
                raf_read.close();
            }
            raf_write.close();
            return mergeFile;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ResponseResult getVideoByCourseIdAndVideoPart(String courseId, String videoPart) {
        MediaFile mediaFile = mediaFileRepository.findFirstByCourseIdAndVideoPart(courseId, videoPart);
        mediaFile.setFileUrl("http://localhost:19003"+"/api/v1/file/" + mediaFile.getFileUrl());
        mediaFile.setPreviewUrlList(mediaFile.getPreviewUrlList().stream().map(item -> {
            return HOST + "/" + item;
        }).collect(Collectors.toList()));
        return ResponseResult.okResult(mediaFile);
    }

    @Override
    public ResponseResult getVideoListByCourseId(String courseId) {
        List<CourseVideoVo> courseVideoVoList = mediaFileRepository.findByCourseId(courseId);
        return ResponseResult.okResult(courseVideoVoList, HOST);
    }

}
