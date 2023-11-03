package com.office.agijagi_back.Controller;

import com.office.agijagi_back.Dto.NoticeDto;
import com.office.agijagi_back.Dto.PostDto;
import com.office.agijagi_back.Service.NoticeService;
import com.office.agijagi_back.Service.RefreshTokenValidateService;
import com.office.agijagi_back.Service.ResponseService;
import com.office.agijagi_back.Util.Response.ListResult;
import com.office.agijagi_back.Util.Response.SingleResult;
import com.office.agijagi_back.Util.S3.S3Service;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Log4j2
@RestController
@RequestMapping("/notice")
public class NoticeController {
    private final ResponseService responseService;
    private final RefreshTokenValidateService refreshTokenValidateService;
    private final NoticeService noticeService;
    private final S3Service s3Service;

    public NoticeController(ResponseService responseService, RefreshTokenValidateService refreshTokenValidateService, NoticeService noticeService, S3Service s3Service) {
        this.refreshTokenValidateService = refreshTokenValidateService;
        this.responseService = responseService;
        this.noticeService = noticeService;
        this.s3Service = s3Service;
    }

    @GetMapping("/noticeTable/{currentPage}/{perPage}")
    public SingleResult<Map> getNoticeTable(@PathVariable @Valid int currentPage,
                                            @PathVariable @Valid int perPage) throws IOException {
        log.info("[NoticeController] getNoticeTable");

        return responseService.getSingleResult(noticeService.getNoticeTable(currentPage, perPage));
    }

}
