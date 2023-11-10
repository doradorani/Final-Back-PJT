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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
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

    @GetMapping("/notices/{currentPage}/{perPage}")
    public SingleResult<Map> getNotices(@PathVariable @Valid int currentPage,
                                        @PathVariable @Valid int perPage) throws IOException {
        log.info("[NoticeController] getNotices");

        return responseService.getSingleResult(noticeService.getNotices(currentPage, perPage));
    }

    @PostMapping("/registNotice")
    public SingleResult<Integer> registNotice(@RequestPart(value = "data") @ApiParam(value = "data", required = true) Map<String, String> data,
                                              @RequestPart(value = "files" , required = false) @ApiParam(value = "files") List<MultipartFile> files) throws IOException {
        log.info("[NoticeController] registNotice");

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String adminId = userDetails.getUsername();
        if (files != null) {
            List<String> fileList = s3Service.uploadListFiles(files);
            return responseService.getSingleResult(noticeService.registNotice(adminId, data, files, fileList));
        }
        return responseService.getSingleResult(noticeService.registNotice(adminId, data, files, null));
    }

    @GetMapping("/noticeDetail/{noticeIndex}/{modifyRequest}")
    public ListResult<NoticeDto> getNoticeDetail(@PathVariable @Valid int noticeIndex, @PathVariable @Valid int modifyRequest) {
        log.info("[NoticeController] getNoticeDetail");

        return responseService.getListResult(noticeService.getNoticeDetail(noticeIndex, modifyRequest));
    }

    @GetMapping("/detail/{noticeIndex}")
    public SingleResult<Map<String, Object>> getNoticeDetailForUser(@PathVariable @Valid int noticeIndex) {
        log.info("[NoticeController] getNoticeDetail");

        return responseService.getSingleResult(noticeService.getNoticeDetailForUser(noticeIndex));
    }

    @DeleteMapping("/deleteNotice/{noticeIndex}")
    public SingleResult<Integer> deleteNotice(@PathVariable @Valid int noticeIndex) throws Exception {
        log.info("[NoticeController] deleteNotice");

        return responseService.getSingleResult(noticeService.deleteNotice(noticeIndex));
    }

    @GetMapping("/recentNotice")
    public SingleResult<Integer> getRecentNotice() {
        log.info("[NoticeController] getRecentNotice");

        return responseService.getSingleResult(noticeService.getRecentNotice());
    }

    @PostMapping("/modifyNotice")
    public SingleResult<Integer> modifyNotice(@RequestPart(value = "data") @ApiParam(value = "data", required = true) Map<String, String> data,
                                              @RequestPart(value = "files" , required = false) @ApiParam(value = "files") List<MultipartFile> files) throws IOException {
        log.info("[NoticeController] modifyNotice");

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String adminId = userDetails.getUsername();
        if (files != null) {
            List<String> fileList = s3Service.uploadListFiles(files);
            return responseService.getSingleResult(noticeService.modifyNotice(adminId, data, files, fileList));
        }
        return responseService.getSingleResult(noticeService.modifyNotice(adminId, data, files, null));
    }
}
