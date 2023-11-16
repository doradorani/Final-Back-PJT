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

    public int deleteChildInfo(int no, String email) {
        log.info("[DiaryService] deleteChildInfo");

        return diaryMapper.deleteChildInfoByNo(no);
    }

    public List<ChildDto> searchChildren(String email) {
        log.info("[DiaryService] searchChildren");

        return diaryMapper.selectChildrenByEmail(email);
    }

    public ChildDto searchChildDetail(int childNo) {
        log.info("[DiaryService] searchChildDetail");

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

        return diaryMapper.selectDailyDiary(childNo);
    }
    public DiaryDto searchDiaryDetail(int childNo, int diaryNo) {
        log.info("[DiaryService] searchDiaryDetail");

        return diaryMapper.selectDailyDetail(childNo, diaryNo);
    }

    public int modifyDailyDiary(DiaryDto diaryDto) {
        log.info("[DiaryService] modifyDailyDiary");

        return diaryMapper.updateDailyDiary(diaryDto);
    }

    public int deleteDailyDiary(int childNo, int diaryNo) {
        log.info("[DiaryService] deleteDailyDiary");

        return diaryMapper.deleteDailyDiary(childNo, diaryNo);
    }

    public List<DiaryDto> searchDailyDiaries(String email) {
        log.info("[DiaryService] searchDailyDiaries");

        return diaryMapper.searchDailyDiaries(email);
    }

    public List<DiaryDto> searchChildRandomPictures(int childNo) {
        log.info("[DiaryService] searchChildRandomPictures");

        return diaryMapper.selectChildRandomPictures(childNo);
    }

    public List<DiaryDto> searchChildrenRandomPictures(String username) {
        log.info("[DiaryService] searchChildrenRandomPictures");

        return diaryMapper.selectChildrenRandomPictures(username);
    }
}
