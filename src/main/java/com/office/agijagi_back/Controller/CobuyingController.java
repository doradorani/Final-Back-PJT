package com.office.agijagi_back.Controller;

import com.office.agijagi_back.Dto.CoBuyProductDto;
import com.office.agijagi_back.Service.CobuyingService;
import com.office.agijagi_back.Service.ResponseService;
import com.office.agijagi_back.Util.Response.SingleResult;
import com.office.agijagi_back.Util.S3.S3Service;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@RestController
@Log4j2
@RequestMapping("/coBuy")
public class CobuyingController {

    private final S3Service s3Service;
    private final ResponseService responseService;
    private final CobuyingService cobuyingService;

    public CobuyingController(S3Service s3Service, ResponseService responseService, CobuyingService cobuyingService) {
        this.s3Service = s3Service;
        this.responseService = responseService;
        this.cobuyingService = cobuyingService;
    }

    @PostMapping(value = "/admin/register", consumes = {"multipart/form-data"})
    public SingleResult<Integer> coBuyRegister(@RequestPart(value = "files", required = false) List<MultipartFile> files,
                                            @RequestPart("info") Map<String, Object> info) throws IOException {

        log.info("coBuyRegister()");

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userName = userDetails.getUsername();

        List<String> filesUrl = s3Service.uploadListFiles(files);

        String imgUrl = "";

        for (int i =0; i< filesUrl.size(); i++){
            imgUrl += filesUrl.get(i);
            if(i != filesUrl.size()-1) imgUrl += ",";
        }

        CoBuyProductDto coBuyProductDto = new CoBuyProductDto();
        coBuyProductDto.setImg(imgUrl);
        coBuyProductDto.setId(userName);
        coBuyProductDto.setName((String) info.get("productName"));
        coBuyProductDto.setContent((String) info.get("productDescription"));

        String minParticipantsStr = (String) info.get("minParticipants");
        try {
            int minParticipants = Integer.parseInt(minParticipantsStr);
            coBuyProductDto.setMin_num(minParticipants);
        } catch (NumberFormatException e) {
            log.info(e);
        }

        coBuyProductDto.setPrice((String) info.get("productPrice"));
        coBuyProductDto.setStart_date((String) info.get("productStart"));
        coBuyProductDto.setEnd_date((String) info.get("productEnd"));

        List<String> productOptions = (List<String>) info.get("productOptions");

        for (int i = 0; i < Math.min(productOptions.size(), 5); i++) {
            String fieldName = "option" + (i + 1);
            String optionValue = productOptions.get(i);

            try {
                setFieldValue(coBuyProductDto, fieldName, optionValue);
            } catch (Exception e) {
                log.info(e);
            }
        }

        return responseService.getSingleResult(cobuyingService.coBuyRegister(coBuyProductDto));
    }

    //옵셥 1~5 필터 필드
    private void setFieldValue(Object obj, String fieldName, Object value) throws IllegalAccessException, NoSuchFieldException {
        log.info("setFieldValue()");

        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(obj, value);
    }

    //전체 상품
    @GetMapping("/list/{currentPage}/{perPage}")
    public SingleResult<Map> coBuyList(@PathVariable @Valid int currentPage,
                                       @PathVariable @Valid int perPage) throws IOException {
        log.info("coBuyList()");

        return responseService.getSingleResult(cobuyingService.coBuyList(currentPage, perPage));
    }

    @GetMapping("/detailProduct/{detailProductNo}")
    public SingleResult<Map> detailProduct(@PathVariable @Valid int detailProductNo) throws IOException{
        log.info("detailProduct()");

        return responseService.getSingleResult(cobuyingService.detailProductNo(detailProductNo));
    }

    @GetMapping("/userDetailProduct/{detailProductNo}")
    public SingleResult<Map> userDetailProduct(@PathVariable @Valid int detailProductNo) throws IOException{
        log.info("userDetailProduct()");

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();

        return responseService.getSingleResult(cobuyingService.userDetailProduct(email, detailProductNo));
    }

    @PostMapping("/fundingProduct/{detailProductNo}/{selectedOption}")
    public SingleResult<Integer> fundingProduct(@PathVariable @Valid int detailProductNo,
                                                @PathVariable @Valid String selectedOption) throws IOException{
        log.info("fundingProduct()");

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();

        return responseService.getSingleResult(cobuyingService.fundingProduct(email, detailProductNo, selectedOption));
    }

    @PostMapping("/cancelFundingProduct/{detailProductNo}")
    public SingleResult<Integer> cancelFundingProduct(@PathVariable @Valid int detailProductNo) throws IOException{
        log.info("cancelFundingProduct()");

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();

        return responseService.getSingleResult(cobuyingService.cancelFundingProduct(email, detailProductNo));
    }

    @GetMapping("/myCobuy")
    public SingleResult<Map> myCobuy(){
        log.info("myCobuy()");

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();

        return responseService.getSingleResult(cobuyingService.myCobuy(email));
    }

    @GetMapping("/coBuyHit/{detailProductNo}")
    public SingleResult<Integer> cobuyHit(@PathVariable @Valid int detailProductNo) throws IOException {
        log.info("cobuyHit()");

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();

        return responseService.getSingleResult(cobuyingService.cobuyHit(email, detailProductNo));
    }

    @GetMapping("/myFundingProduct/{currentPage}/{perPage}")
    public SingleResult<Map> myFundingProduct(@PathVariable @Valid int currentPage,
                                              @PathVariable @Valid int perPage) throws IOException {
        log.info("myFundingProduct()");

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();

        return responseService.getSingleResult(cobuyingService.myFundingProduct(email, currentPage, perPage));
    }

    @GetMapping("/myHitProduct/{currentPage}/{perPage}")
    public SingleResult<Map> myHitProduct(@PathVariable @Valid int currentPage,
                                              @PathVariable @Valid int perPage) throws IOException {
        log.info("myLikeProduct()");

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();

        return responseService.getSingleResult(cobuyingService.myHitProduct(email, currentPage, perPage));
    }
}
