package com.office.agijagi_back.Controller;

import com.office.agijagi_back.Dto.CoBuyProductDto;
import com.office.agijagi_back.Dto.UserDto;
import com.office.agijagi_back.Service.CobuyingService;
import com.office.agijagi_back.Service.ResponseService;
import com.office.agijagi_back.Util.Response.SingleResult;
import com.office.agijagi_back.Util.S3.S3Service;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
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


    @ApiOperation(httpMethod = "POST"
            , value = "상품 등록 admin.ver"
            , notes = "admin regist product"
            , response = Integer.class
            , responseContainer = "SingleResult")
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
        coBuyProductDto.setMin_num((String) info.get("minParticipants"));
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

    @ApiOperation(httpMethod = "PUT"
            , value = "상품 수정 admin.ver"
            , notes = "admin modify product"
            , response = Integer.class
            , responseContainer = "SingleResult")
    @PutMapping(value = "/admin/modify", consumes = {"multipart/form-data"})
    public SingleResult<Integer> coBuyModify(@RequestPart(value = "files", required = false) List<MultipartFile> files,
                                               @RequestPart("info") Map<String, Object> info) throws IOException {

        log.info("coBuyModify()");

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userName = userDetails.getUsername();

        ArrayList<String> productImgList = (ArrayList<String>) info.get("productImg");
        String existImg = String.join(",", productImgList);
        List<String> filesUrl = s3Service.uploadListFiles(files);

        String imgUrl = "";
        if(existImg != null && !existImg.equals("")) {
            imgUrl += existImg;
            if(filesUrl.size() > 0){
                imgUrl += ",";
            }
        }

        for (int i =0; i< filesUrl.size(); i++){
            imgUrl += filesUrl.get(i);
            if(i != filesUrl.size()-1) imgUrl += ",";
        }

        CoBuyProductDto coBuyProductDto = new CoBuyProductDto();

        String productNoString = (String) info.get("productNo");
        int productNo = Integer.parseInt(productNoString);
        coBuyProductDto.setNo(productNo);

        coBuyProductDto.setImg(imgUrl);
        coBuyProductDto.setId(userName);
        coBuyProductDto.setName((String) info.get("productName"));
        coBuyProductDto.setContent((String) info.get("productDescription"));
        coBuyProductDto.setMin_num((String) info.get("minParticipants"));
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

        return responseService.getSingleResult(cobuyingService.coBuyModify(coBuyProductDto));
    }

    //옵셥 1~5 필터 필드
    private void setFieldValue(Object obj, String fieldName, Object value) throws IllegalAccessException, NoSuchFieldException {
        log.info("setFieldValue()");

        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(obj, value);
    }

    @ApiOperation(httpMethod = "GET"
            , value = "전체 상품 all.ver"
            , notes = "registed product list"
            , response = Map.class
            , responseContainer = "SingleResult")
    @GetMapping("/list/{optionList}/{currentPage}/{perPage}")
    public SingleResult<Map> coBuyList(@PathVariable @Valid String optionList,
                                       @PathVariable @Valid int currentPage,
                                       @PathVariable @Valid int perPage) throws IOException {
        log.info("coBuyList()");

        return responseService.getSingleResult(cobuyingService.coBuyList(optionList, currentPage, perPage));
    }

    @ApiOperation(httpMethod = "GET"
            , value = "진행 필터 상품 user.ver"
            , notes = "proceed filter product"
            , response = Map.class
            , responseContainer = "SingleResult")
    @GetMapping("/list/{status}/{optionList}/{currentPage}/{perPage}")
    public SingleResult<Map> coBuyProceed(@PathVariable @Valid String status,
                                        @PathVariable @Valid String optionList,
                                        @PathVariable @Valid int currentPage,
                                        @PathVariable @Valid int perPage) throws IOException {
        log.info("coBuyProceed()");

        return responseService.getSingleResult(cobuyingService.coBuyProceed(status, optionList, currentPage, perPage));
    }

    @ApiOperation(httpMethod = "GET"
            , value = "상품 상세보기 admin.ver"
            , notes = "show detail product admin ver"
            , response = Map.class
            , responseContainer = "SingleResult")
    @GetMapping("/detailProduct/{detailProductNo}")
    public SingleResult<Map> detailProduct(@PathVariable @Valid int detailProductNo) throws IOException{
        log.info("detailProduct()");

        return responseService.getSingleResult(cobuyingService.detailProductNo(detailProductNo));
    }

    @ApiOperation(httpMethod = "GET"
            , value = "상품 상세보기 user.ver"
            , notes = "show detail product user ver"
            , response = Map.class
            , responseContainer = "SingleResult")
    @GetMapping("/userDetailProduct/{detailProductNo}")
    public SingleResult<Map> userDetailProduct(@PathVariable @Valid int detailProductNo) throws IOException{
        log.info("userDetailProduct()");

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();

        return responseService.getSingleResult(cobuyingService.userDetailProduct(email, detailProductNo));
    }

    @ApiOperation(httpMethod = "PUT"
            , value = "상품 펀딩하기 user.ver"
            , notes = "user funding product"
            , response = Integer.class
            , responseContainer = "SingleResult")
    @PutMapping("/fundingProduct/{detailProductNo}/{selectedOption}")
    public SingleResult<Integer> fundingProduct(@PathVariable @Valid int detailProductNo,
                                                @PathVariable @Valid String selectedOption) throws IOException{
        log.info("fundingProduct()");

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();

        return responseService.getSingleResult(cobuyingService.fundingProduct(email, detailProductNo, selectedOption));
    }

    @ApiOperation(httpMethod = "PUT"
            , value = "상품 펀딩 취소하기 user.ver"
            , notes = "user cancel funding product"
            , response = Integer.class
            , responseContainer = "SingleResult")
    @PutMapping("/cancelFundingProduct/{detailProductNo}")
    public SingleResult<Integer> cancelFundingProduct(@PathVariable @Valid int detailProductNo) throws IOException{
        log.info("cancelFundingProduct()");

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();

        return responseService.getSingleResult(cobuyingService.cancelFundingProduct(email, detailProductNo));
    }

    @ApiOperation(httpMethod = "GET"
            , value = "나의 좋아요-펀딩 상품 가져오기 user.ver"
            , notes = "get my hit and funding product by user"
            , response = Map.class
            , responseContainer = "SingleResult")
    @GetMapping("/myCobuy")
    public SingleResult<Map> myCobuy(){
        log.info("myCobuy()");

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();

        return responseService.getSingleResult(cobuyingService.myCobuy(email));
    }

    @ApiOperation(httpMethod = "GET"
            , value = "상품 좋아요 user.ver"
            , notes = "click product hit"
            , response = Integer.class
            , responseContainer = "SingleResult")
    @GetMapping("/coBuyHit/{detailProductNo}")
    public SingleResult<Integer> cobuyHit(@PathVariable @Valid int detailProductNo) throws IOException {
        log.info("cobuyHit()");

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();

        return responseService.getSingleResult(cobuyingService.cobuyHit(email, detailProductNo));
    }

    @ApiOperation(httpMethod = "GET"
            , value = "나의 펀딩 상품 보기 user.ver"
            , notes = "show my funding product"
            , response = Map.class
            , responseContainer = "SingleResult")
    @GetMapping("/myFundingProduct/{currentPage}/{perPage}")
    public SingleResult<Map> myFundingProduct(@PathVariable @Valid int currentPage,
                                              @PathVariable @Valid int perPage) throws IOException {
        log.info("myFundingProduct()");

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();

        return responseService.getSingleResult(cobuyingService.myFundingProduct(email, currentPage, perPage));
    }

    @ApiOperation(httpMethod = "GET"
            , value = "나의 좋아요 상품 보기 user.ver"
            , notes = "show my hit product"
            , response = Map.class
            , responseContainer = "SingleResult")
    @GetMapping("/myHitProduct/{currentPage}/{perPage}")
    public SingleResult<Map> myHitProduct(@PathVariable @Valid int currentPage,
                                              @PathVariable @Valid int perPage) throws IOException {
        log.info("myLikeProduct()");

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();

        return responseService.getSingleResult(cobuyingService.myHitProduct(email, currentPage, perPage));
    }

    @ApiOperation(httpMethod = "GET"
            , value = "배너 User.ver"
            , notes = "Random banner"
            , response = Map.class
            , responseContainer = "SingleResult")
    @GetMapping("/randomBanner/{num}")
    public SingleResult<Map> randomBanner(@PathVariable @Valid int num) throws IOException {
        log.info("randomBanner()");

        return responseService.getSingleResult(cobuyingService.randomBanner(num));
    }
}
