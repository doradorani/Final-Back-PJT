package com.office.agijagi_back.Controller;

import com.office.agijagi_back.Dto.ChildDto;
import com.office.agijagi_back.Dto.DiaryDto;
import com.office.agijagi_back.Service.DiaryService;
import com.office.agijagi_back.Service.RefreshTokenValidateService;
import com.office.agijagi_back.Service.ResponseService;
import com.office.agijagi_back.Util.Response.ListResult;
import com.office.agijagi_back.Util.Response.SingleResult;
import com.office.agijagi_back.Util.S3.S3Service;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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

    @ApiOperation(httpMethod = "POST"
            , value = "자녀 정보 등록"
            , notes = "insert child information"
            , response = Integer.class
            , responseContainer = "SingleResult")
    @PostMapping(value = "/childInfo",
            consumes = {"multipart/form-data"})
    public SingleResult<Integer> registerChild(@RequestPart(value = "data") @ApiParam(value = "data", required = true) Map<String, String> data,
                                               @RequestPart(value = "file") @ApiParam(value = "file", required = true) MultipartFile file,
                                               HttpServletRequest request) throws IOException {
        log.info("[DiaryController] registerChild");

        String email = refreshTokenValidateService.refreshTokenValidate(request);

        if (data != null) {
            String imgUrl = s3Service.uploadFile(file);
            ChildDto childDto = new ChildDto(0, data.get("name"), email, imgUrl, data.get("birth_date"), null, null);

            return responseService.getSingleResult(diaryService.registerChild(childDto));
        }
        return responseService.getSingleResult(0);
    }
    @ApiOperation(httpMethod = "POST"
            , value = "자녀 정보 및 사진 수정"
            , notes = "update child information"
            , response = Integer.class
            , responseContainer = "SingleResult")
    @PostMapping("/childInfo/{childNo}")
    public SingleResult<Integer> modifyChild(@RequestPart(value = "data", required = false) @ApiParam(value = "data", required = false) Map<String, String> data,
                                             @RequestPart(value = "file", required = false) @ApiParam(value = "file", required = false) MultipartFile file,
                                             HttpServletRequest request,
                                             @PathVariable int childNo) throws IOException {
        log.info("[DiaryController] updateChild");

        String email = refreshTokenValidateService.refreshTokenValidate(request);
        String imgUrl = "";
        if(file != null){
            imgUrl = s3Service.uploadFile(file);
        }

        ChildDto childDto = new ChildDto(childNo, data.get("name"), email, imgUrl, data.get("birth_date"), null, null);

        return responseService.getSingleResult(diaryService.modifyChild(childDto));
    }

    @ApiOperation(httpMethod = "GET"
            , value = "해당 계정의 자녀들의 정보"
            , notes = "select all children's information"
            , response = ChildDto.class
            , responseContainer = "ListResult")
    @GetMapping("/childrenInfo")
    public ListResult<ChildDto> searchChildren() {
        log.info("[DiaryController] searchChildren");

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String email = userDetails.getUsername();

        return responseService.getListResult(diaryService.searchChildren(email));
    }

    @ApiOperation(httpMethod = "GET"
            , value = "선택한 자녀의 모든 정보"
            , notes = "insert child information"
            , response = Integer.class
            , responseContainer = "SingleResult")
    @GetMapping("/childrenDetail/{childNo}")
    public SingleResult<ChildDto> searchChildDetail(@ApiParam(value = "childNo") @PathVariable int childNo) {
        log.info("[DiaryController] searchChildren");
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();

        return responseService.getSingleResult(diaryService.searchChildDetail(childNo));
    }
    @ApiOperation(httpMethod = "DELETE"
            , value = "선택한 자녀의 정보 삭제"
            , notes = "insert child information"
            , response = Integer.class
            , responseContainer = "SingleResult")
    @DeleteMapping("/childBook/{childNo}")
    public SingleResult<Integer> deleteChild(@ApiParam(value = "childNo") @PathVariable int childNo) {
        log.info("[DiaryController] deleteChild");

        return responseService.getSingleResult(diaryService.deleteChildInfo(childNo));
    }

    @ApiOperation(httpMethod = "POST"
            , value = "해당 자녀의 일기 등록"
            , notes = "insert child daily diary"
            , response = Integer.class
            , responseContainer = "SingleResult")
    @PostMapping("/dailyDiary/{childNo}")
    public SingleResult<Integer> registerDailyDiary(@RequestPart(value = "data") @ApiParam(value = "data", required = true) Map<String, String> data,
                                                    @RequestPart(value = "file") @ApiParam(value = "file", required = true) @Valid MultipartFile file,
                                                    HttpServletRequest request,
                                                    @PathVariable int childNo) throws IOException {
        log.info("[DiaryController] registerDailyDiary");
        String email = refreshTokenValidateService.refreshTokenValidate(request);

        DiaryDto diaryDto = new DiaryDto(0, email, childNo, data.get("title"), data.get("content"), s3Service.uploadFile(file), null, null);


        return responseService.getSingleResult(diaryService.registerDailyDiary(diaryDto));
    }

    @ApiOperation(httpMethod = "GET"
            , value = "해당 자녀의 일기 조회"
            , notes = "select child daily diaries"
            , response = DiaryDto.class
            , responseContainer = "ListResult")
    @GetMapping("/dailyDiary/{childNo}")
    public ListResult<DiaryDto> searchDailyDiary(@PathVariable int childNo){
        log.info("[DiaryController] searchDailyDiary");

        return responseService.getListResult(diaryService.searchDailyDiary(childNo));
    }
    @ApiOperation(httpMethod = "POST"
            , value = "해당 자녀의 일기 수정"
            , notes = "modify child daily diary"
            , response = DiaryDto.class
            , responseContainer = "SingleResult")
    @PostMapping("/dailyDiary/{childNo}/{diaryNo}")
    public SingleResult<Integer> modifyDailyDiary(@RequestPart(value = "data") @ApiParam(value = "data", required = true) Map<String, String> data,
                                                @RequestPart(value = "file") @ApiParam(value = "file", required = true) MultipartFile file,
                                                HttpServletRequest request,
                                                @PathVariable int childNo,
                                                @PathVariable int diaryNo) throws IOException {
        log.info("[DiaryController] updateDailyDiary");
        String email = refreshTokenValidateService.refreshTokenValidate(request);

        DiaryDto diaryDto = new DiaryDto(diaryNo, email, childNo, data.get("title"), data.get("content"), s3Service.uploadFile(file),null, null);

        return responseService.getSingleResult(diaryService.modifyDailyDiary(diaryDto));
    }
    @ApiOperation(httpMethod = "DELETE"
            , value = "해당 자녀의 일기 삭제"
            , notes = "delete child daily diary"
            , response = DiaryDto.class
            , responseContainer = "SingleResult")
    @DeleteMapping("/dailyDiary/{childNo}/{diaryNo}")
    public SingleResult<Integer> deleteDailyDiary(@PathVariable int childNo,
                                                @PathVariable int diaryNo) throws IOException {
        log.info("[DiaryController] deleteDailyDiary");

        return responseService.getSingleResult(diaryService.deleteDailyDiary(childNo, diaryNo));
    }



}
