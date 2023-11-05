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


    public List<ChildDto> registerChild(ChildDto childDto) {
        log.info("[DiarySerivce] storeChildInfo");

        int result = diaryMapper.insertChildInfo(childDto);
        return diaryMapper.selectChildrenByEmail(childDto.getU_email());
    }

    public List<ChildDto> deleteChildInfo(int no, String email) {
        log.info("[DiaryService] deleteChildInfo");

         diaryMapper.deleteChildInfoByNo(no);
        return diaryMapper.selectChildrenByEmail(email);
    }

    public List<ChildDto> searchChildren(String email) {
        log.info("[DiaryService] searchChildren");

        return diaryMapper.selectChildrenByEmail(email);
    }

    public ChildDto searchChildDetail(int childNo) {
        log.info("[DiaryService] searchChildren");

        return diaryMapper.selectChildByNo(childNo);
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
