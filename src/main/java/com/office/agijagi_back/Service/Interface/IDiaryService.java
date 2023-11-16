package com.office.agijagi_back.Service.Interface;

import com.office.agijagi_back.Dto.ChildDto;
import com.office.agijagi_back.Dto.DiaryDto;

import java.util.List;

public interface IDiaryService {

    int registerChild(ChildDto childDto);

    int deleteChildInfo(int no, String email);

    List<ChildDto> searchChildren(String email);

    ChildDto searchChildDetail(int childNo);

    int modifyChild(ChildDto childDto);

    int registerDailyDiary(DiaryDto diaryDto);

    List<DiaryDto> searchDailyDiary(int childNo);
    DiaryDto searchDiaryDetail(int childNo, int diaryNo);

    int modifyDailyDiary(DiaryDto diaryDto);

    int deleteDailyDiary(int childNo, int diaryNo);

    List<DiaryDto> searchDailyDiaries(String email);

    List<DiaryDto> searchChildRandomPictures(int childNo);

     List<DiaryDto> searchChildrenRandomPictures(String username);
}
