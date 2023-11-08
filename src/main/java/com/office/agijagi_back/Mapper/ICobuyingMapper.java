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
}
