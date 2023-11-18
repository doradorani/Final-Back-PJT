package com.office.agijagi_back.Mapper;

import com.office.agijagi_back.Dto.ChildDto;
import com.office.agijagi_back.Dto.DiaryDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IDiaryMapper {
    int insertChildInfo(ChildDto childDto);

    int deleteChildInfoByNo(String no, String email);

    List<ChildDto> selectChildrenByEmail(String email);

    ChildDto selectChildByNo(String no, String email);

    int updateChild(ChildDto childDto);

    int insertDailyDiary(DiaryDto diaryDto);

    List<DiaryDto> selectDailyDiaries(String childNo);

    int updateDailyDiary(DiaryDto diaryDto);

    int deleteDailyDiary(String childNo, String diaryNo, String email);

    DiaryDto selectDailyDetail(String childNo, String diaryNo, String email);

    List<DiaryDto> selectDailyDiary(String childNo, String email);

    List<DiaryDto> searchDailyDiaries(String email);

    int selectChildCount(String uEmail);

    List<DiaryDto> selectChildRandomPictures(String childNo, String email);

    List<DiaryDto> selectChildrenRandomPictures(String username);
}
