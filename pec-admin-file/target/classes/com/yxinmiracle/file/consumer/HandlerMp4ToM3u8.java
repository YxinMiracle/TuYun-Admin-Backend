package com.yxinmiracle.file.consumer;

import com.alibaba.fastjson.JSON;
import com.yxinmiracle.file.mapper.MediaFileRepository;
import com.yxinmiracle.model.common.enums.AppHttpCodeEnum;
import com.yxinmiracle.model.file.pojos.MediaFile;
import com.yxinmiracle.model.file.pojos.MediaFileProcess_m3u8;
import com.yxinmiracle.utils.common.HlsVideoUtil;
import com.yxinmiracle.utils.common.TxFileUtil;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class HandlerMp4ToM3u8 {

    @Autowired
    public MediaFileRepository mediaFileRepository;

    @Value("${ffmpeg-path}")
    public String ffmpegPath;

    @Value("${pec-service-manage-media.upload-location}")
    public String servicePath;

    @RabbitListener(queues = "queue_media_video_processor",containerFactory = "customContainerFactory")
    public void receiveMediaProcessTask(String msg){
        // 得到mediaId
        Map map = JSON.parseObject(msg, Map.class);
        String mediaId = (String) map.get("mediaId");
        // 从数据库中查询media的信息
        Optional<MediaFile> optional = mediaFileRepository.findById(mediaId);
        if (!optional.isPresent()){
            return;
        }
        MediaFile mediaFile = optional.get();
        if (!mediaFile.getFileType().equals("mp4")){
            mediaFile.setFileStatus(AppHttpCodeEnum.VIDEO_PROCESS_ERROR.getCode()+"");
            mediaFileRepository.save(mediaFile);
            return;
        }else {
            // 需要处理
            mediaFile.setFileStatus(AppHttpCodeEnum.VIDEO_PROCESSING.getCode()+""); // 改为处理中
            mediaFileRepository.save(mediaFile);
        }
        // 要处理的文件路径
        String videoPath = servicePath +mediaFile.getFilePath() + mediaFile.getFileName();
        // m3u8_name
        String m3u8Name = mediaFile.getFileId() + ".m3u8";
        String m3u8folderPath = servicePath + mediaFile.getFilePath() + "hls/";
        String previewPictureFolderPath = servicePath + mediaFile.getFilePath() + "preview/";
        HlsVideoUtil hlsVideoUtil = new HlsVideoUtil(ffmpegPath,videoPath,m3u8Name,m3u8folderPath,previewPictureFolderPath,mediaFile.getFileId());
        String tsResult = hlsVideoUtil.generateM3u8();
        if (tsResult == null || !tsResult.equals("success")){
            mediaFile.setProcessStatus(AppHttpCodeEnum.VIDEO_PROCESS_ERROR.getCode()+"");
            MediaFileProcess_m3u8 mediaFileProcess_m3u8 = new MediaFileProcess_m3u8();
            mediaFileProcess_m3u8.setErrormsg(AppHttpCodeEnum.VIDEO_PROCESS_ERROR.getErrorMessage());
            mediaFile.setMediaFileProcess_m3u8(mediaFileProcess_m3u8);
            mediaFileRepository.save(mediaFile);
            return;
        }
        // 处理成功
        mediaFile.setProcessStatus(AppHttpCodeEnum.SUCCESS.getCode()+"");
        // 获取ts列表
        List<String> ts_list = hlsVideoUtil.get_ts_list();
        MediaFileProcess_m3u8 mediaFileProcess_m3u8 = new MediaFileProcess_m3u8();
        mediaFileProcess_m3u8.setTslist(ts_list);
        mediaFile.setMediaFileProcess_m3u8(mediaFileProcess_m3u8);

        String fileUrl = mediaFile.getFilePath() + "hls/" + m3u8Name;
        mediaFile.setFileUrl(fileUrl);

        // 设置视频时间
        String video_time = hlsVideoUtil.get_video_time(servicePath + mediaFile.getFilePath() + mediaFile.getFileName());
        mediaFile.setVideoTime(video_time);

        // 进行获取预览列表的设置
        List<String> previewPictureUploadList = TxFileUtil.videoPreviewPictureUpload(previewPictureFolderPath, mediaFile.getFilePath());
        mediaFile.setPreviewUrlList(previewPictureUploadList);
        mediaFileRepository.save(mediaFile);
    }
}
