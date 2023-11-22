package com.office.agijagi_back.Service.Interface;

import com.office.agijagi_back.Dto.CoBuyProductDto;

import java.util.List;
import java.util.Map;

public interface ICobuyingService {

    public int coBuyRegister(CoBuyProductDto coBuyProductDto);
    public int coBuyListCnt();
    public Map<String, Object> coBuyList(String optionList, int currentPage, int perPage);
    public Map<String, Object> detailProductNo(int detailProductNo);
    public int fundingProduct(String email, int detailProductNo, String selectedOption);
    public Map<String, Object> myCobuy(String email);
    public int cobuyHit(String email, int detailProductNo);
    public Map<String, Object> myFundingProduct(String email, int currentPage, int perPage);
    public Map<String, Object> myHitProduct(String email, int currentPage, int perPage);
    public Map<String, Object> userDetailProduct(String email, int detailProductNo);
    public int cancelFundingProduct(String email, int detailProductNo);
    public Map<String, Object> coBuyProceed(String status, String optionList, int currentPage, int perPage);
    public int coBuyModify(CoBuyProductDto coBuyProductDto);
    public Map<String, Object> randomBanner(int num);
}
