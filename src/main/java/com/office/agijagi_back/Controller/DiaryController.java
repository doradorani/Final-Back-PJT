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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

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

    private String getUserEmail(){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return userDetails.getUsername();
    }

    @ApiOperation(httpMethod = "POST"
            , value = "자녀 정보 등록"
            , notes = "insert child information"
            , response = Integer.class
            , responseContainer = "SingleResult")
    @PostMapping(value = "/childInfo")
    public SingleResult<Integer> registerChild(@RequestPart(value = "file") @ApiParam(value = "file", required = true) MultipartFile file,
                                               @RequestPart(value = "data") @ApiParam(value = "data", required = true) ChildDto childDto) throws IOException {
        log.info("[DiaryController] registerChild");

        childDto.setU_email(getUserEmail());
        if (childDto != null) {
            String imgUrl = s3Service.uploadFile(file);
            childDto.setImg(imgUrl);
            return responseService.getSingleResult(diaryService.registerChild(childDto));
        }
        return responseService.getSingleResult(null);
    }

    @ApiOperation(httpMethod = "POST"
            , value = "자녀 정보 및 사진 수정"
            , notes = "update child information"
            , response = Integer.class
            , responseContainer = "SingleResult")
    @PostMapping("/childInfo/{childNo}")
    public SingleResult<Integer> modifyChild(@RequestPart(value = "file", required = false) @ApiParam(value = "file", required = false) MultipartFile file,
                                             @RequestPart(value = "data") @ApiParam(value = "data", required = false) ChildDto childDto,
                                             @PathVariable int childNo) throws IOException {
        log.info("[DiaryController] updateChild");

        childDto.setU_email(getUserEmail());
        childDto.setNo(childNo);

        String imgUrl = "";
        if (file != null) {
            imgUrl = s3Service.uploadFile(file);
            childDto.setImg(imgUrl);
        }

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

        return responseService.getListResult(diaryService.searchChildren(getUserEmail()));
    }

    @ApiOperation(httpMethod = "GET"
            , value = "선택한 자녀의 모든 정보"
            , notes = "insert child information"
            , response = Integer.class
            , responseContainer = "SingleResult")
    @GetMapping("/childrenDetail/{childNo}")
    public SingleResult<ChildDto> searchChildDetail(@ApiParam(value = "childNo") @PathVariable String childNo) {
        log.info("[DiaryController] searchChildren");

        return responseService.getSingleResult(diaryService.searchChildDetail(childNo, getUserEmail()));
    }

    @ApiOperation(httpMethod = "DELETE"
            , value = "선택한 자녀의 정보 삭제"
            , notes = "insert child information"
            , response = Integer.class
            , responseContainer = "SingleResult")
    @DeleteMapping("/childBook/{childNo}")
    public SingleResult<Integer> deleteChild(@ApiParam(value = "childNo") @PathVariable String childNo) {
        log.info("[DiaryController] deleteChild");

        return responseService.getSingleResult(diaryService.deleteChildInfo(childNo, getUserEmail()));
    }

    @ApiOperation(httpMethod = "POST"
            , value = "해당 자녀의 일기 등록"
            , notes = "insert child daily diary"
            , response = Integer.class
            , responseContainer = "SingleResult")
    @PostMapping("/dailyDiary/{childNo}")
    public SingleResult<Integer> registerDailyDiary(@RequestPart(value = "file") @ApiParam(value = "file", required = true) MultipartFile file,
                                                    @RequestPart(value = "data") @ApiParam(value = "data", required = true) DiaryDto diaryDto,
                                                    @PathVariable int childNo) throws IOException {
        log.info("[DiaryController] registerDailyDiary");

        String imgUrl = "";
        if (file != null) {
            imgUrl = s3Service.uploadFile(file);
            diaryDto.setImg(imgUrl);
        }
        diaryDto.setU_email(getUserEmail());
        diaryDto.setCd_no(childNo);

        return responseService.getSingleResult(diaryService.registerDailyDiary(diaryDto));
    }

    @ApiOperation(httpMethod = "GET"
            , value = "모든 자녀의 일기 조회"
            , notes = "select all children daily diaries"
            , response = DiaryDto.class
            , responseContainer = "ListResult")
    @GetMapping("/dailyDiaries")
    public ListResult<DiaryDto> searchDailyDiaries() {
        log.info("[DiaryController] searchDailyDiaries");

        return responseService.getListResult(diaryService.searchDailyDiaries(getUserEmail()));
    }

    @ApiOperation(httpMethod = "GET"
            , value = "해당 자녀의 일기 조회"
            , notes = "select child daily diaries"
            , response = DiaryDto.class
            , responseContainer = "ListResult")
    @GetMapping("/dailyDiary/{childNo}")
    public ListResult<DiaryDto> searchDailyDiary(@PathVariable @ApiParam String childNo) {
        log.info("[DiaryController] searchDailyDiary");

        return responseService.getListResult(diaryService.searchDailyDiary(childNo, getUserEmail()));
    }


    @ApiOperation(httpMethod = "GET"
            , value = "해당 자녀의 선택한 일기 상세 조회"
            , notes = "select child daily diary"
            , response = DiaryDto.class
            , responseContainer = "SingleResult")
    @GetMapping("/dailyDiaryDetail/{childNo}/{diaryNo}")
    public SingleResult<DiaryDto> searchDiaryDetail(@PathVariable @ApiParam String childNo, @PathVariable @ApiParam String diaryNo) {
        log.info("[DiaryController] searchDiaryDetail");

        return responseService.getSingleResult(diaryService.searchDiaryDetail(childNo, diaryNo, getUserEmail()));
    }

    @ApiOperation(httpMethod = "POST"
            , value = "해당 자녀의 일기 수정"
            , notes = "modify child daily diary"
            , response = DiaryDto.class
            , responseContainer = "SingleResult")
    @PostMapping("/dailyDiaryDetail/{childNo}/{diaryNo}")
    public SingleResult<Integer> modifyDailyDiary(@RequestPart(value = "file", required = false) @ApiParam(value = "file", required = true) MultipartFile file,
                                                  @RequestPart(value = "data") @ApiParam(value = "data", required = true) DiaryDto diaryDto,
                                                  @PathVariable int childNo,
                                                  @PathVariable int diaryNo) throws IOException {
        log.info("[DiaryController] updateDailyDiary");
        diaryDto.setU_email(getUserEmail());
        diaryDto.setNo(diaryNo);
        diaryDto.setCd_no(childNo);
        String imgUrl = "";
        if (file != null) {
            imgUrl = s3Service.uploadFile(file);
            diaryDto.setImg(imgUrl);
        }

        return responseService.getSingleResult(diaryService.modifyDailyDiary(diaryDto));
    }

    @ApiOperation(httpMethod = "DELETE"
            , value = "해당 자녀의 일기 삭제"
            , notes = "delete child daily diary"
            , response = DiaryDto.class
            , responseContainer = "SingleResult")
    @DeleteMapping("/dailyDiary/{childNo}/{diaryNo}")
    public SingleResult<Integer> deleteDailyDiary(@PathVariable String childNo,
                                                  @PathVariable String diaryNo) throws IOException {
        log.info("[DiaryController] deleteDailyDiary");

        return responseService.getSingleResult(diaryService.deleteDailyDiary(childNo, diaryNo, getUserEmail()));
    }

    @ApiOperation(httpMethod = "GET"
            , value = "해당 자녀의 사진 랜덤 조회"
            , notes = "select child pictures randomly"
            , response = DiaryDto.class
            , responseContainer = "ListResult")
    @GetMapping("/childPictures/{childNo}")
    public ListResult<DiaryDto> searchChildRandomPictures(@PathVariable @ApiParam String childNo) {
        log.info("[DiaryController] searchChildRandomPictures");

        return responseService.getListResult(diaryService.searchChildRandomPictures(childNo, getUserEmail()));
    }

    @ApiOperation(httpMethod = "GET"
            , value = "해당 계정의 자녀 사진 랜덤 조회"
            , notes = "select children pictures randomly"
            , response = DiaryDto.class
            , responseContainer = "ListResult")
    @GetMapping("/childrenPictures")
    public ListResult<DiaryDto> searchChildrenRandomPictures() {
        log.info("[DiaryController] searchChildrenRandomPictures");
;

        return responseService.getListResult(diaryService.searchChildrenRandomPictures(getUserEmail()));
    }


}
