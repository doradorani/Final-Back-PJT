package com.office.agijagi_back.Mapper;

import com.office.agijagi_back.Dto.ChildDto;
import com.office.agijagi_back.Dto.DiaryDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IDiaryMapper {
    int insertChildInfo(ChildDto childDto);

    int deleteChildInfoByNo(int no);

    List<ChildDto> selectChildrenByEmail(String email);

    ChildDto selectChildByNo(int no);

    int updateChild(ChildDto childDto);

    int insertDailyDiary(DiaryDto diaryDto);

    List<DiaryDto> selectDailyDiaries(int childNo);

    int updateDailyDiary(DiaryDto diaryDto);

    int deleteDailyDiary(int childNo, int diaryNo);

    DiaryDto selectDailyDetail(int childNo, int diaryNo);

    List<DiaryDto> selectDailyDiary(int childNo);

    List<DiaryDto> searchDailyDiaries(String email);

    int selectChildCount(String uEmail);
}
