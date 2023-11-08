package com.office.agijagi_back.Service;

import com.office.agijagi_back.Dto.CoBuyProductDto;
import com.office.agijagi_back.Dto.NoticeDto;
import com.office.agijagi_back.Mapper.ICobuyingMapper;
import com.office.agijagi_back.Service.Interface.ICobuyingService;
import lombok.extern.log4j.Log4j2;
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
    public Map<String, Object> coBuyList(int currentPage, int perPage) {
        log.info("coBuyList()");

        int offset = (currentPage - 1) * perPage;

        List<CoBuyProductDto> coBuyProductDtos = iCobuyingMapper.productList(perPage, offset);
        int productListCnt = coBuyListCnt();

        int totalPages = iCobuyingMapper.productTotalPage(perPage);

        Map<String, Object> CoBuyMap = new HashMap<>();
        CoBuyMap.put("coBuyProductDtos", coBuyProductDtos);
        CoBuyMap.put("totalPages", totalPages);
        CoBuyMap.put("productListCnt", productListCnt);

        return CoBuyMap;
    }

    @Override
    public Map<String, Object> detailProductNo(int detailProductNo) {

        CoBuyProductDto coBuyProductDto = iCobuyingMapper.detailProductNo(detailProductNo);

        int accumulateUser = iCobuyingMapper.accumulateProduct(detailProductNo);

        coBuyProductDto.setAccumulate(accumulateUser);

        Map<String, Object> CoBuyDetailMap = new HashMap<>();
        CoBuyDetailMap.put("coBuyDetailProduct", coBuyProductDto);

        return CoBuyDetailMap;
    }


}
