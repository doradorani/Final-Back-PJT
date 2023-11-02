package com.office.agijagi_back.Service;

import com.office.agijagi_back.Dto.ChildDto;
import com.office.agijagi_back.Dto.DiaryDto;
import com.office.agijagi_back.Mapper.IDiaryMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public int deleteChildInfo(int no) {
        log.info("[DiaryService] deleteChildInfo");

        return diaryMapper.deleteChildInfoByNo(no);
    }

    public List<ChildDto> searchChildren(String email) {
        log.info("[DiaryService] searchChildren");

        return diaryMapper.selectChildrenByEmail(email);
    }

    public ChildDto searchChildDetail(int no) {
        log.info("[DiaryService] searchChildren");

        return diaryMapper.selectChildByNo(no);
    }

    public int modifyChild(ChildDto childDto) {
        log.info("[DiaryService] updateChild");

        return diaryMapper.updateChild(childDto);
    }

    public int registerDailyDiary(DiaryDto diaryDto) {
        log.info("[DiaryService] registerDailyDiary");

        return diaryMapper.insertDailyDiary(diaryDto);
    }

    public List<DiaryDto> searchDailyDiary(int childNo) {
        log.info("[DiaryService] searchDailyDiary");

        return diaryMapper.selectDailyDiaries(childNo);
    }

    public int modifyDailyDiary(DiaryDto diaryDto) {
        log.info("[DiaryService] modifyDailyDiary");

        return diaryMapper.updateDailyDiary(diaryDto);
    }

    public int deleteDailyDiary(int childNo, int diaryNo) {
        log.info("[DiaryService] modifyDailyDiary");

        return diaryMapper.deleteDailyDiary(childNo, diaryNo);
    }
}
