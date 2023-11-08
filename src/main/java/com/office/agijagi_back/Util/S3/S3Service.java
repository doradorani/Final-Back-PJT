package com.office.agijagi_back.Util.S3;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
public class S3Service{
    private final S3Uploader s3Uploader;

    public S3Service(S3Uploader s3Uploader) {
        this.s3Uploader = s3Uploader;
    }


    public String uploadFile(MultipartFile file) throws IOException {
        String url = "";
        if(file != null)  url = s3Uploader.putS3(file);

        return url;
    }

    public void deleteFile(String path) throws Exception {
        String result = "";
        if(path != null) s3Uploader.deleteS3(path);

    }

    public List<String> uploadListFiles(List<MultipartFile> files) throws IOException {
        log.info("[S3Service] uploadListFiles");
        List<String> Lists = new ArrayList<>();
        if (files != null){
            for (int i= 0; i < files.size(); i++){
                String url = s3Uploader.putS3(files.get(i));
                Lists.add(url);
            }
        }
        return Lists;
    }

//    public List<String> uploadListFiles(MultipartFile[] files) throws IOException {
//        log.info("[S3Service] uploadListFiles");
//        List<String> Lists = new ArrayList<>();
//        if (files != null){
//            for (int i= 0; i < files.length; i++){
//                String url = s3Uploader.putS3(files[i]);
//                Lists.add(url);
//            }
//        }
//        return Lists;
//    }
}