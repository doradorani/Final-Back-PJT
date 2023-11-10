package com.office.agijagi_back.Mapper;

import com.office.agijagi_back.Dto.CoBuyProductDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ICobuyingMapper {

    int coBuyRegister(CoBuyProductDto coBuyProductDto);
    int coBuyListCnt();
    List<CoBuyProductDto> productList(int perPage, int offset);
    int productTotalPage(int perPage);
    CoBuyProductDto detailProductNo(int detailProductNo);
    int accumulateProduct(int detailProductNo);
    int fundingProduct(String email, int detailProductNo, String selectedOption);
    int alreadyFunding(String email, int detailProductNo);
    List<Integer> myFundings(String email);
    List<Integer> myHits(String email);
    int alreadyHit(String email, int detailProductNo);
    int cobuyInsertHit(String email, int detailProductNo);
    int cobuyDeleteHit(String email, int detailProductNo);
    int decreaseHit(int detailProductNo);
    int increaseHit(int detailProductNo);
    int getCobuyHitByNo(int detailProductNo);
    List<CoBuyProductDto> myFundingProduct(String email, int perPage, int offset);
    int fundingListCnt(String email);
    int fundingTotalPage(String email, int perPage);
    List<CoBuyProductDto> myHitProduct(String email, int perPage, int offset);
    int hitListCnt(String email);
    int hitTotalPage(String email, int perPage);
    String myCobuyOption(String email, int detailProductNo);
    int cancelFundingProduct(String email, int detailProductNo);
}
