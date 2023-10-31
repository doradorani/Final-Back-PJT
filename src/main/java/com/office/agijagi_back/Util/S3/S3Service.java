package com.office.agijagi_back.Util.S3;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
}