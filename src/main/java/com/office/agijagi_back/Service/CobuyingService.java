package com.office.agijagi_back.Service;

import com.office.agijagi_back.Dto.CoBuyProductDto;
import com.office.agijagi_back.Dto.NoticeDto;
import com.office.agijagi_back.Mapper.ICobuyingMapper;
import com.office.agijagi_back.Service.Interface.ICobuyingService;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@Service
public class CobuyingService implements ICobuyingService {

    private final ICobuyingMapper iCobuyingMapper;

    public CobuyingService(ICobuyingMapper iCobuyingMapper) {
        this.iCobuyingMapper = iCobuyingMapper;
    }

    @Override
    public int coBuyRegister(CoBuyProductDto coBuyProductDto) {
        log.info("coBuyRegister()");

        return iCobuyingMapper.coBuyRegister(coBuyProductDto);
    }

    @Override
    public int coBuyListCnt() {
        log.info("coBuyListCnt()");

        return iCobuyingMapper.coBuyListCnt();
    }

    @Override
    public Map<String, Object> coBuyList(String optionList, int currentPage, int perPage) {
        log.info("coBuyList()");

        int offset = (currentPage - 1) * perPage;

        List<CoBuyProductDto> coBuyProductDtos = iCobuyingMapper.productList(optionList, perPage, offset);
        int totalPages = iCobuyingMapper.productTotalPage(perPage);
        int productListCnt = coBuyListCnt();

        Map<String, Object> CoBuyMap = new HashMap<>();
        CoBuyMap.put("coBuyProductDtos", coBuyProductDtos);
        CoBuyMap.put("totalPages", totalPages);
        CoBuyMap.put("productListCnt", productListCnt);

        return CoBuyMap;
    }

    @Override
    public Map<String, Object> coBuyProceed(String status, String optionList, int currentPage, int perPage) {
        log.info("coBuyProceed()");

        int offset = (currentPage - 1) * perPage;

        List<CoBuyProductDto> coBuyProductDtos = iCobuyingMapper.productProceed(status, optionList, perPage, offset);;

        int totalPages = iCobuyingMapper.productTotalPage(perPage);
        int productListCnt = coBuyListCnt();

        Map<String, Object> CoBuyMap = new HashMap<>();
        CoBuyMap.put("coBuyProductDtos", coBuyProductDtos);
        CoBuyMap.put("totalPages", totalPages);
        CoBuyMap.put("productListCnt", productListCnt);

        return CoBuyMap;
    }

    @Override
    public Map<String, Object> detailProductNo(int detailProductNo) {
        log.info("detailProductNo()");

        Map<String, Object> CoBuyDetailMap = new HashMap<>();

        CoBuyProductDto coBuyProductDto = iCobuyingMapper.detailProductNo(detailProductNo);
        int accumulateUser = iCobuyingMapper.accumulateProduct(detailProductNo);
        coBuyProductDto.setAccumulate(accumulateUser);

        CoBuyDetailMap.put("coBuyDetailProduct", coBuyProductDto);

        return CoBuyDetailMap;
    }

    @Override
    public Map<String, Object> userDetailProduct(String email, int detailProductNo) {
        log.info("userDetailProduct()");

        Map<String, Object> CoBuyDetailMap = new HashMap<>();

        CoBuyProductDto coBuyProductDto = iCobuyingMapper.detailProductNo(detailProductNo);
        int accumulateUser = iCobuyingMapper.accumulateProduct(detailProductNo);
        coBuyProductDto.setAccumulate(accumulateUser);

        String myCobuyOption = iCobuyingMapper.myCobuyOption(email, detailProductNo);

        CoBuyDetailMap.put("coBuyDetailProduct", coBuyProductDto);
        CoBuyDetailMap.put("myCobuyOption", myCobuyOption);

        return CoBuyDetailMap;
    }

    @Override
    public int fundingProduct(String email, int detailProductNo, String selectedOption) {
        log.info("fundingProduct()");

        int alreadyFunding = iCobuyingMapper.alreadyFunding(email, detailProductNo);
        if(alreadyFunding > 0){
            return 0;
        }

        return iCobuyingMapper.fundingProduct(email, detailProductNo, selectedOption);
    }

    @Override
    public int cancelFundingProduct(String email, int detailProductNo) {
        log.info("cancelFundingProduct()");

        return iCobuyingMapper.cancelFundingProduct(email, detailProductNo);
    }



    @Override
    public Map<String, Object> myCobuy(String email) {
        log.info("myCobuy()");

        List<Integer> myFundings = iCobuyingMapper.myFundings(email);
        List<Integer> myHits = iCobuyingMapper.myHits(email);

        Map<String, Object> myCobuyList = new HashMap<>();
        myCobuyList.put("myFundings", myFundings);
        myCobuyList.put("myHits", myHits);

        return myCobuyList;
    }

    @Override
    public int cobuyHit(String email, int detailProductNo) {
        log.info("cobuyHit()");

        int alreadyHit = iCobuyingMapper.alreadyHit(email, detailProductNo);

        if(alreadyHit > 0){
            int deletedHit = iCobuyingMapper.cobuyDeleteHit(email, detailProductNo);
            int decreasedHit = iCobuyingMapper.decreaseHit(detailProductNo);

            if(deletedHit > 0 && decreasedHit > 0){
                return iCobuyingMapper.getCobuyHitByNo(detailProductNo);
            }
        }
        else{
            int insertHit = iCobuyingMapper.cobuyInsertHit(email, detailProductNo);
            int increaseHit = iCobuyingMapper.increaseHit(detailProductNo);

            if(insertHit > 0 && increaseHit > 0){
                return iCobuyingMapper.getCobuyHitByNo(detailProductNo);
            }
        }
        return 0;
    }

    @Override
    public Map<String, Object> myFundingProduct(String email, int currentPage, int perPage) {
        log.info("myFundingProduct()");

        int offset = (currentPage - 1) * perPage;

        List<CoBuyProductDto> myFundingProducts = iCobuyingMapper.myFundingProduct(email, perPage, offset);
        int productListCnt = iCobuyingMapper.fundingListCnt(email);
        int totalPages = iCobuyingMapper.fundingTotalPage(email, perPage);

        Map<String, Object> CoBuyMap = new HashMap<>();
        CoBuyMap.put("myFundingProducts", myFundingProducts);
        CoBuyMap.put("productListCnt", productListCnt);
        CoBuyMap.put("totalPages", totalPages);

        return CoBuyMap;
    }

    @Override
    public Map<String, Object> myHitProduct(String email, int currentPage, int perPage) {
        log.info("myLikeProduct()");

        int offset = (currentPage - 1) * perPage;

        List<CoBuyProductDto> myHitProducts = iCobuyingMapper.myHitProduct(email, perPage, offset);
        int productListCnt = iCobuyingMapper.hitListCnt(email);
        int totalPages = iCobuyingMapper.hitTotalPage(email, perPage);

        Map<String, Object> CoBuyMap = new HashMap<>();
        CoBuyMap.put("myHitProducts", myHitProducts);
        CoBuyMap.put("productListCnt", productListCnt);
        CoBuyMap.put("totalPages", totalPages);

        return CoBuyMap;
    }




}
