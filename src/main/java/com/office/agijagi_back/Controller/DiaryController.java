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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
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
    @PostMapping(value = "/childInfo")
    public SingleResult<Integer> registerChild(@RequestPart(value = "data") @ApiParam(value = "data", required = true) Map<String, String> data,
                                               @RequestPart(value = "file") @ApiParam(value = "file", required = true) MultipartFile file,
                                               HttpServletRequest request) throws IOException {
        log.info("[DiaryController] registerChild");

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();

        if (data != null) {
            String imgUrl = s3Service.uploadFile(file);
            ChildDto childDto = new ChildDto(0, data.get("name"), 0, email, imgUrl, data.get("content"), data.get("birth_date"), null, null);

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
    public SingleResult<Integer> modifyChild(@RequestPart(value = "data") @ApiParam(value = "data", required = false) Map<String, String> data,
                                             @RequestPart(value = "file", required = false) @ApiParam(value = "file", required = false) MultipartFile file,
                                             @PathVariable int childNo) throws IOException {
        log.info("[DiaryController] updateChild");

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();

        String imgUrl = "";
        if (file != null) {
            imgUrl = s3Service.uploadFile(file);
        }

        ChildDto childDto = new ChildDto(childNo, data.get("name"), 0, email, imgUrl, data.get("content"), data.get("birth_date"), null, null);

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
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();

        return responseService.getSingleResult(diaryService.deleteChildInfo(childNo, email));
    }

    @ApiOperation(httpMethod = "POST"
            , value = "해당 자녀의 일기 등록"
            , notes = "insert child daily diary"
            , response = Integer.class
            , responseContainer = "SingleResult")
    @PostMapping("/dailyDiary/{childNo}")
    public SingleResult<Integer> registerDailyDiary(@RequestPart(value = "data") @ApiParam(value = "data", required = true) DiaryDto diaryDto,
                                                    @RequestPart(value = "file") @ApiParam(value = "file", required = true) MultipartFile file,
                                                    @PathVariable int childNo) throws IOException {
        log.info("[DiaryController] registerDailyDiary");
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();
        String imgUrl = "";
        if (file != null) {
            imgUrl = s3Service.uploadFile(file);
        }

        diaryDto.setU_email(email);
        diaryDto.setImg(imgUrl);

//        DiaryDto diaryDto = new DiaryDto(0, email, childNo, 0,data.get("fourcuts_checked"), null, data.get("title"), data.get("content"), imgUrl, null, null);


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
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();

        return responseService.getListResult(diaryService.searchDailyDiaries(email));
    }

    @ApiOperation(httpMethod = "GET"
            , value = "해당 자녀의 일기 조회"
            , notes = "select child daily diaries"
            , response = DiaryDto.class
            , responseContainer = "ListResult")
    @GetMapping("/dailyDiary/{childNo}")
    public ListResult<DiaryDto> searchDailyDiary(@PathVariable @ApiParam int childNo) {
        log.info("[DiaryController] searchDailyDiary");

        return responseService.getListResult(diaryService.searchDailyDiary(childNo));
    }


    @ApiOperation(httpMethod = "GET"
            , value = "해당 자녀의 선택한 일기 상세 조회"
            , notes = "select child daily diary"
            , response = DiaryDto.class
            , responseContainer = "SingleResult")
    @GetMapping("/dailyDiaryDetail/{childNo}/{diaryNo}")
    public SingleResult<DiaryDto> searchDiaryDetail(@PathVariable @ApiParam int childNo, @PathVariable @ApiParam int diaryNo) {
        log.info("[DiaryController] searchDiaryDetail");

        return responseService.getSingleResult(diaryService.searchDiaryDetail(childNo, diaryNo));
    }

    @ApiOperation(httpMethod = "POST"
            , value = "해당 자녀의 일기 수정"
            , notes = "modify child daily diary"
            , response = DiaryDto.class
            , responseContainer = "SingleResult")
    @PostMapping("/dailyDiaryDetail/{childNo}/{diaryNo}")
    public SingleResult<Integer> modifyDailyDiary(@RequestPart(value = "data") @ApiParam(value = "data", required = true) Map<String, String> data,
                                                  @RequestPart(value = "file", required = false) @ApiParam(value = "file", required = true) MultipartFile file,
                                                  @PathVariable int childNo,
                                                  @PathVariable int diaryNo) throws IOException {
        log.info("[DiaryController] updateDailyDiary");
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();
        String imgUrl = "";
        if (file != null) {
            imgUrl = s3Service.uploadFile(file);
        }

//        DiaryDto diaryDto = new DiaryDto(diaryNo, email, childNo, 0,null, data.get("title"), data.get("content"), imgUrl, null, null);
        DiaryDto diaryDto = null;

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

    @ApiOperation(httpMethod = "GET"
            , value = "해당 자녀의 사진 랜덤 조회"
            , notes = "select child pictures randomly"
            , response = DiaryDto.class
            , responseContainer = "ListResult")
    @GetMapping("/childPictures/{childNo}")
    public ListResult<DiaryDto> searchChildRandomPictures(@PathVariable @ApiParam int childNo) {
        log.info("[DiaryController] searchChildRandomPictures");

        return responseService.getListResult(diaryService.searchChildRandomPictures(childNo));
    }

    @ApiOperation(httpMethod = "GET"
            , value = "해당 계정의 자녀 사진 랜덤 조회"
            , notes = "select children pictures randomly"
            , response = DiaryDto.class
            , responseContainer = "ListResult")
    @GetMapping("/childrenPictures")
    public ListResult<DiaryDto> searchChildrenRandomPictures() {
        log.info("[DiaryController] searchChildrenRandomPictures");

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return responseService.getListResult(diaryService.searchChildrenRandomPictures(userDetails.getUsername()));
    }



}
