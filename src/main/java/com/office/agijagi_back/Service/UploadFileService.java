package com.office.agijagi_back.Service;


import com.office.agijagi_back.Util.UploadFile.UploadFileDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@Log4j2
@Service
public class UploadFileService {

    public UploadFileDto upload(String student_id, MultipartFile file) {
        log.info("upload()");

        boolean result = false;
        UploadFileDto uploadFileDto = new UploadFileDto();

        // File 저장
        String fileOriName = file.getOriginalFilename();
        String fileExtension = fileOriName.substring(fileOriName.lastIndexOf("."), fileOriName.length());
        String uploadDir = "c:\\dormease\\freeboard\\upload\\" + student_id;

        UUID uuid = UUID.randomUUID();
        String uniqueName = uuid.toString().replaceAll("-", "");

        File saveFile = new File(uploadDir + "\\" + uniqueName + fileExtension);
        if (!saveFile.exists())
            saveFile.mkdirs();
        try {
            file.transferTo(saveFile);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (result) {
            log.info("FILE UPLOAD SUCCESS!!");
            uploadFileDto.setBoard_attach_file(uniqueName + fileExtension);
            uploadFileDto.setOri_file_name(fileOriName);
            uploadFileDto.setDir_name(uploadDir);

            return uploadFileDto;

        } else {
            log.info("FILE UPLOAD FAIL!!");

            return null;
        }
    }
}