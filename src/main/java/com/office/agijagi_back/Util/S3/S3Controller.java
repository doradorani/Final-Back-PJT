package com.office.agijagi_back.Util.S3;

import com.office.agijagi_back.Service.ResponseService;
import com.office.agijagi_back.Util.Response.SingleResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/test")
public class S3Controller {

    private final S3Service s3Service;
    private final ResponseService responseService;

    public S3Controller(S3Service s3Service, ResponseService responseService) {
        this.s3Service = s3Service;
        this.responseService = responseService;
    }

//    @PostMapping(path = "/test", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @PostMapping("/upload")
    public SingleResult<String> upload(
            @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {

        return responseService.getSingleResult(s3Service.uploadFile(file));
    }
    @PostMapping("/delete")
    public void delete(
            @RequestPart(value = "path") String path) throws Exception {
        s3Service.deleteFile(path);
//        return responseService.getSingleResult(s3Service.deleteFile(path));
    }
}
