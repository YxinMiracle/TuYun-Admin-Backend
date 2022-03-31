package com.yxinmiracle.file;

import com.yxinmiracle.file.mapper.MediaFileRepository;
import com.yxinmiracle.model.file.pojos.MediaFile;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestProcessBuilder {
    @Autowired
    private MediaFileRepository mediaFileRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    public void testProcessBulder() throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("ping","127.0.0.1");
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        InputStream inputStream = process.getInputStream();
        InputStreamReader reader = new InputStreamReader(inputStream,"GBK");
        int len = -1;
        char[] chars = new char[1024];
        while ((len = reader.read(chars))!=-1){
            String string = new String(chars,0,len);
            System.out.println(string);
        }
        inputStream.close();
        reader.close();
    }

    @Test
    public void testMongo(){
        MediaFile mediaFile = new MediaFile();
        mediaFile.setFileId("12123123");
        mediaFile.setFilePath("asfsdfsdf");
        mediaFileRepository.save(mediaFile);
    }

    @Test
    public void testMONGODVB(){
        MediaFile mediaFile = mediaFileRepository.findFirstByCourseIdAndVideoPart("2","1");
        System.out.println(mediaFile);
    }


    @Test
    public void printName(){
        System.out.println("My name is: wuzhouyang");
    }
}
