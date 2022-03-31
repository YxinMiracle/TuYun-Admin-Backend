package com.yxinmiracle.file.mapper;

import com.yxinmiracle.model.file.pojos.MediaFile;
import com.yxinmiracle.model.serives.vos.CourseVideoVo;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface MediaFileRepository extends MongoRepository<MediaFile,String> {

    MediaFile findFirstByCourseIdAndVideoPart(String courseId,String videoPart);

    List<CourseVideoVo> findByCourseId(String courseId);

    MediaFile findFirstByFileName(String fileName);

}
