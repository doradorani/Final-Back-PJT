package com.office.agijagi_back.Service.Interface;

import com.office.agijagi_back.Dto.ChildDto;
import com.office.agijagi_back.Dto.DiaryDto;

import java.util.List;

public interface IDiaryService {

    int registerChild(ChildDto childDto);

    int deleteChildInfo(String no, String email);

    List<ChildDto> searchChildren(String email);

    ChildDto searchChildDetail(String childNo, String email);

    int modifyChild(ChildDto childDto);

    int registerDailyDiary(DiaryDto diaryDto);

    List<DiaryDto> searchDailyDiary(String childNo, String email);
    DiaryDto searchDiaryDetail(String childNo, String diaryNo, String email);

    int modifyDailyDiary(DiaryDto diaryDto);

    int deleteDailyDiary(String childNo, String diaryNo, String email);

    List<DiaryDto> searchDailyDiaries(String email);

    List<DiaryDto> searchChildRandomPictures(String childNo, String email);

     List<DiaryDto> searchChildrenRandomPictures(String email);

}
