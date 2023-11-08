package com.office.agijagi_back.Controller;

import com.office.agijagi_back.Dto.ChildNoteDto;
import com.office.agijagi_back.Service.ChildNoteService;
import com.office.agijagi_back.Service.ResponseService;
import com.office.agijagi_back.Util.Response.ListResult;
import com.office.agijagi_back.Util.Response.SingleResult;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@Log4j2
@Controller
@RequestMapping("/childHealth")
public class ChildNoteController {

    private final ChildNoteService childNoteService;
    private final ResponseService responseService;

    public ChildNoteController(ChildNoteService childNoteService, ResponseService responseService) {
        this.childNoteService = childNoteService;
        this.responseService = responseService;
    }
    @ApiOperation(httpMethod = "POST"
            , value = "해당 자녀의 건강기록 등록"
            , notes = "register child Note"
            , response = Integer.class
            , responseContainer = "SingleResult")
    @PostMapping("/childNote/{childNo}")
    public SingleResult<Integer> registerChildNote(@ApiParam ChildNoteDto childNoteDto,
                                                   @PathVariable String childNo){
        log.info("[ChildNoteController] registerChildNote");
        UserDetails userDetails  = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        childNoteDto.setU_email(userDetails.getUsername());

        return responseService.getSingleResult(childNoteService.registerChildNote(childNoteDto));
    }

    @ApiOperation(httpMethod = "GET"
            , value = "해당 자녀의 건강기록 조회"
            , notes = "search child Note"
            , response = ChildNoteDto.class
            , responseContainer = "ListResult")
    @GetMapping("/childNotes/{childNo}")
    public SingleResult<Map<String, Object>> searchChildNotes(@PathVariable String childNo){
        log.info("[ChildNoteController] searchChildNotes");
        UserDetails userDetails  = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return responseService.getSingleResult(childNoteService.searchChildNotes(childNo,userDetails.getUsername()));
    }

    @ApiOperation(httpMethod = "GET"
            , value = "모든 자녀의 접종내역 조회"
            , notes = "search children Inoculation Notes"
            , response = ChildNoteDto.class
            , responseContainer = "ListResult")
    @GetMapping("/inoculationNotes")
    public ListResult<ChildNoteDto> searchChildrenInoculationNotes(){
        log.info("[ChildNoteController] searchChildrenInoculationNotes");
        UserDetails userDetails  = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return responseService.getListResult(childNoteService.searchChildrenInoculationNotes(userDetails.getUsername()));
    }


}
