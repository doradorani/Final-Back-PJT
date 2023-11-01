package com.office.agijagi_back.Controller;

import com.office.agijagi_back.Dto.ChildDto;
import com.office.agijagi_back.Service.DiaryService;
import com.office.agijagi_back.Service.RefreshTokenValidateService;
import com.office.agijagi_back.Service.ResponseService;
import com.office.agijagi_back.Util.Response.SingleResult;
import com.office.agijagi_back.Util.S3.S3Service;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;

@Log4j2
@RestController
@RequestMapping("/diary")
public class DiaryController {

    private final ResponseService responseService;
    private final RefreshTokenValidateService refreshTokenValidateService;
    private final DiaryService diaryService;
    private final S3Service s3Service;

    public DiaryController(ResponseService responseService, RefreshTokenValidateService refreshTokenValidateService, DiaryService diaryService, S3Service s3Service) {
        this.responseService = responseService;
        this.refreshTokenValidateService = refreshTokenValidateService;
        this.diaryService = diaryService;
        this.s3Service = s3Service;
    }

    @PostMapping("/childInfo")
    public SingleResult<Integer> registerChild( @RequestPart(value = "data", required = false) Map<String, String> data, @RequestPart(value = "file", required = false) MultipartFile file, HttpServletRequest request) throws IOException {
        log.info("[DiaryController] registerChild");

        String email = refreshTokenValidateService.refreshTokenValidate(request);

        if( data != null){
            String imgUrl = s3Service.uploadFile(file);
            ChildDto childDto = new ChildDto(0,data.get("name"), email, imgUrl, data.get("birth_date"),  null, null );

            return responseService.getSingleResult(diaryService.registerChild(childDto));
        }
        return responseService.getSingleResult(0);
    }
}
