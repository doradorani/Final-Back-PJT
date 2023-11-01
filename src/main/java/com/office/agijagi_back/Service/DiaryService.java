package com.office.agijagi_back.Service;

import com.office.agijagi_back.Dto.ChildDto;
import com.office.agijagi_back.Mapper.IDiaryMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class DiaryService {

    private final IDiaryMapper diaryMapper;

    public DiaryService(IDiaryMapper diaryMapper) {
        this.diaryMapper = diaryMapper;
    }


    public int registerChild(ChildDto childDto) {
        log.info("[DiarySerivce] storeChildInfo");

        return diaryMapper.insertChildInfo(childDto);
    }
}
