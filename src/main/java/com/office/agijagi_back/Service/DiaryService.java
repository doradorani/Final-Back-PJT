package com.office.agijagi_back.Service;

import com.office.agijagi_back.Dto.ChildDto;
import com.office.agijagi_back.Dto.DiaryDto;
import com.office.agijagi_back.Mapper.IDiaryMapper;
import com.office.agijagi_back.Service.Interface.IDiaryService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
public class DiaryService implements IDiaryService {

    private final IDiaryMapper diaryMapper;

    public DiaryService(IDiaryMapper diaryMapper) {
        this.diaryMapper = diaryMapper;
    }


    public int registerChild(ChildDto childDto) {
        log.info("[DiarySerivce] registerChild");
        int count = diaryMapper.selectChildCount(childDto.getU_email());
        childDto.setSequence(count + 1);

        return diaryMapper.insertChildInfo(childDto);
    }

    public int deleteChildInfo(String no, String email) {
        log.info("[DiaryService] deleteChildInfo");

        return diaryMapper.deleteChildInfoByNo(no, email);
    }

    public List<ChildDto> searchChildren(String email) {
        log.info("[DiaryService] searchChildren");

        return diaryMapper.selectChildrenByEmail(email);
    }

    public ChildDto searchChildDetail(String childNo, String email) {
        log.info("[DiaryService] searchChildDetail");

        return diaryMapper.selectChildByNo(childNo, email);
    }

    public int modifyChild(ChildDto childDto) {
        log.info("[DiaryService] updateChild");

        return diaryMapper.updateChild(childDto);
    }

    public int registerDailyDiary(DiaryDto diaryDto) {
        log.info("[DiaryService] registerDailyDiary");

        return diaryMapper.insertDailyDiary(diaryDto);
    }

    public List<DiaryDto> searchDailyDiary(String childNo, String email) {
        log.info("[DiaryService] searchDailyDiary");

        return diaryMapper.selectDailyDiary(childNo, email);
    }
    public DiaryDto searchDiaryDetail(String childNo, String diaryNo, String email) {
        log.info("[DiaryService] searchDiaryDetail");

        return diaryMapper.selectDailyDetail(childNo, diaryNo, email);
    }

    public int modifyDailyDiary(DiaryDto diaryDto) {
        log.info("[DiaryService] modifyDailyDiary");

        return diaryMapper.updateDailyDiary(diaryDto);
    }

    public int deleteDailyDiary(String childNo, String diaryNo, String email) {
        log.info("[DiaryService] deleteDailyDiary");

        return diaryMapper.deleteDailyDiary(childNo, diaryNo, email);
    }

    public List<DiaryDto> searchDailyDiaries(String email) {
        log.info("[DiaryService] searchDailyDiaries");

        return diaryMapper.searchDailyDiaries(email);
    }

    public List<DiaryDto> searchChildRandomPictures(String childNo, String email) {
        log.info("[DiaryService] searchChildRandomPictures");

        return diaryMapper.selectChildRandomPictures(childNo, email);
    }

    public List<DiaryDto> searchChildrenRandomPictures(String email) {
        log.info("[DiaryService] searchChildrenRandomPictures");

        return diaryMapper.selectChildrenRandomPictures(email);
    }
}
