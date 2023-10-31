package com.office.agijagi_back.Controller;

import com.office.agijagi_back.Dto.ChildDto;
import com.office.agijagi_back.Service.DiaryService;
import com.office.agijagi_back.Service.ResponseService;
import com.office.agijagi_back.Util.Response.SingleResult;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping("/diary")
public class DiaryController {

    private final ResponseService responseService;
    private final DiaryService diaryService;

    public DiaryController(ResponseService responseService, DiaryService diaryService) {
        this.responseService = responseService;
        this.diaryService = diaryService;
    }

    @PostMapping("/childInfo")
    public SingleResult<Integer> storeChildInfo(ChildDto childDto) {

//        return responseService.getSingleResult(diaryService.storeChildInfo(childDto));
        return null;
    }
}
