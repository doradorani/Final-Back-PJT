package com.office.agijagi_back.Controller;

import com.office.agijagi_back.Dto.PostReportDto;
import com.office.agijagi_back.Service.CommunityService;
import com.office.agijagi_back.Service.RefreshTokenValidateService;
import com.office.agijagi_back.Service.ReportService;
import com.office.agijagi_back.Service.ResponseService;
import com.office.agijagi_back.Util.Response.ListResult;
import com.office.agijagi_back.Util.Response.SingleResult;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@Log4j2
@RestController
@RequestMapping("/report")
public class ReportController {

    private final ResponseService responseService;

    private final RefreshTokenValidateService refreshTokenValidateService;

    private final ReportService reportService;

    public ReportController(ResponseService responseService, RefreshTokenValidateService refreshTokenValidateService, ReportService reportService) {
        this.responseService = responseService;
        this.refreshTokenValidateService = refreshTokenValidateService;
        this.reportService = reportService;
    }
    @ApiOperation(httpMethod = "GET"
            , value = "해당 페이지 게시물 신고 조회"
            , notes = "select posts in a page"
            , response = Map.class
            , responseContainer = "SingleResult")
    @GetMapping("/postReportTable/{currentPage}/{perPage}")
    public SingleResult<Map> getPostReports(@PathVariable @Valid int currentPage, @PathVariable @Valid int perPage) {
        log.info("[ReportController] getPostReports");

        return responseService.getSingleResult(reportService.getPostReports(currentPage, perPage));
    }
    @ApiOperation(httpMethod = "GET"
            , value = "해당 페이지 댓글 신고 조회"
            , notes = "select reply reports in a page"
            , response = Map.class
            , responseContainer = "SingleResult")
    @GetMapping("/replyReportTable/{currentPage}/{perPage}")
    public SingleResult<Map> getReplyReports(@PathVariable @Valid int currentPage, @PathVariable @Valid int perPage) {
        log.info("[ReportController] getPostReports");

        return responseService.getSingleResult(reportService.getReplyReports(currentPage, perPage));
    }
    @ApiOperation(httpMethod = "DELETE"
            , value = "해당 신고된 게시물 삭제"
            , notes = "delete reported post"
            , response = Integer.class
            , responseContainer = "SingleResult")
    @DeleteMapping("/deletePostReport/{postIndex}/{reportIndex}")
    public SingleResult<Integer> deletePostReport(@PathVariable @Valid int postIndex, @PathVariable @Valid int reportIndex) {
        log.info("[ReportController] deletePostReport");

        return responseService.getSingleResult(reportService.deletePostReport(postIndex, reportIndex));
    }
}
