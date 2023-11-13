package com.office.agijagi_back.Util.S3;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

@Service
@Log4j2
public class S3Uploader {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public S3Uploader(AmazonS3Client amazonS3Client) {
        this.amazonS3Client = amazonS3Client;
    }

    /**
     * 로컬 경로에 저장
     */
//    public String uploadFileToS3(MultipartFile multipartFile, String filePath) {
//        // MultipartFile -> File 로 변환
//        File uploadFile = null;
//        try {
//            uploadFile = convert(multipartFile)
//                    .orElseThrow(() -> new IllegalArgumentException("[error]: MultipartFile -> 파일 변환 실패"));
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//        // S3에 저장된 파일 이름
//        String fileName = filePath + "/" + UUID.randomUUID();
//
//        // s3로 업로드 후 로컬 파일 삭제
//        String uploadImageUrl = putS3(uploadFile, fileName);
//        removeNewFile(uploadFile);
//        return uploadImageUrl;
//    }


    /**
     * S3로 업로드
     * @param uploadFile : 업로드할 파일
     * @return 업로드 경로
     */
    public String putS3(MultipartFile uploadFile) throws IOException {
        String originalFilename = uploadFile.getOriginalFilename();
//        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."), originalFilename.length());
        UUID uuid = UUID.randomUUID();
        String uniqueName = uuid.toString().replaceAll("-", "");

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(uploadFile.getSize());
        metadata.setContentType(uploadFile.getContentType());

        amazonS3Client.putObject(bucket, uniqueName, uploadFile.getInputStream(), metadata);
        return amazonS3Client.getUrl(bucket, uniqueName).toString();
    }

    /**
     * S3에 있는 파일 삭제
     * 영어 파일만 삭제 가능 -> 한글 이름 파일은 안됨
     * 임의로 파일 이름 설정해서 올렸으므로 한글 이름 파일은 없음
     */
    public void deleteS3(String filePath) throws Exception {
        log.info("[S3Uploader] : S3에 있는 파일 삭제");
        try{
            String key = filePath.substring(59); // 폴더/파일.확장자

            try {
                amazonS3Client.deleteObject(bucket, key);
            } catch (AmazonServiceException e) {
                log.info(e.getErrorMessage());
            }

        } catch (Exception exception) {
            log.info(exception.getMessage());
        }
    }

    /**
     * file 다운로드
     *
     * @param fileKey  파일 key 로 해당 버킷에서 파일 찾아서 들고옴
     * @param filePath 다운로드 url
     * @param request
     * @param response
     * @return
     */
    public boolean download(String filePath, HttpServletRequest request, HttpServletResponse response) {
        String downloadFileName = filePath.substring(59);
        S3Object fullObject = null;
        try {
            fullObject = amazonS3Client.getObject(bucket, downloadFileName);
            if (fullObject == null) {
                return false;
            }
        } catch (AmazonS3Exception e) {
        }

        OutputStream os = null;
        FileInputStream fis = null;
        boolean success = false;
        try {
            S3ObjectInputStream objectInputStream = fullObject.getObjectContent();
            byte[] bytes = IOUtils.toByteArray(objectInputStream);

            response.setContentType("application/octet-stream;charset=UTF-8");
            response.setHeader("Content-Transfer-Encoding", "binary");
            response.setHeader( "Content-Disposition", "attachment; filename=\"" + downloadFileName + "\";" );
            response.setHeader("Content-Length", String.valueOf(fullObject.getObjectMetadata().getContentLength()));
            response.setHeader("Set-Cookie", "fileDownload=true; path=/");
            FileCopyUtils.copy(bytes, response.getOutputStream());
            success = true;
        } catch (IOException e) {
            log.debug(e.getMessage(), e);
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                log.debug(e.getMessage(), e);
            }
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                log.debug(e.getMessage(), e);
            }
        }
        return success;
    }


    //    /**
//     * 로컬에 저장된 파일 지우기
//     * @param targetFile : 저장된 파일
//     */
//    private void removeNewFile(File targetFile) {
//        if (targetFile.delete()) {
//            log.info("[파일 업로드] : 파일 삭제 성공");
//            return;
//        }
//        log.info("[파일 업로드] : 파일 삭제 실패");
//    }
//
//    /**
//     * 로컬에 파일 업로드 및 변환
//     * @param file : 업로드할 파일
//     */
//    private Optional<File> convert(MultipartFile file) throws IOException {
//        // 로컬에서 저장할 파일 경로 : user.dir => 현재 디렉토리 기준
//        String dirPath = System.getProperty("user.dir") + "/" + file.getOriginalFilename();
//        File convertFile = new File(dirPath);
//
//        if (convertFile.createNewFile()) {
//            // FileOutputStream 데이터를 파일에 바이트 스트림으로 저장
//            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
//                fos.write(file.getBytes());
//            }
//            return Optional.of(convertFile);
//        }
//
//        return Optional.empty();
//    }
    // 폴더 생성
    public void createFolder(String bucketName, String folderName) {
        amazonS3Client.putObject(bucketName, folderName + "/", new ByteArrayInputStream(new byte[0]), new ObjectMetadata());
    }

    // 다중 파일 업로드
//    public void fileUpload(List<MultipartFile> files, List<String> fileList) throws Exception {
//        if (amazonS3Client != null) {
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
//            Date date = new Date();
//            String today = sdf.format(date);
//
//            if (!files.isEmpty()) {
//                createFolder(bucket + "/contact", today);
//            }
//
//            ObjectMetadata objectMetadata = new ObjectMetadata();
//            for (int i = 0; i < files.size(); i++) {
//                objectMetadata.setContentType(files.get(i).getContentType());
//                objectMetadata.setContentLength(files.get(i).getSize());
//                objectMetadata.setHeader("filename", files.get(i).getOriginalFilename());
//                amazonS3Client.putObject(new PutObjectRequest(bucket + "/contact/" + today, fileList.get(i), files.get(i).getInputStream(), objectMetadata));
//            }
//        } else {
////        throw new AppException(ErrorCode.aws_credentials_fail, null);
//        }
//    }
//
//    // 다중 파일 삭제
//    public void fileDelete(String filePath, String fileName) {
//        if (amazonS3Client != null) {
//            amazonS3Client.deleteObject(new DeleteObjectRequest(filePath, fileName));
//        } else {
////        throw new AppException(ErrorType.aws_credentials_fail, null);
//        }
//    }
}
