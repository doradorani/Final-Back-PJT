package com.office.agijagi_back.Mapper;

import com.office.agijagi_back.Dto.NoticeDto;
import com.office.agijagi_back.Dto.PostDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface INoticeMapper {
    List<NoticeDto> selectNoticeTableList(int perPage, int offset);
    int selectNoticeTotalPage(int perPage);
    List<NoticeDto> selectNotices(int perPage, int offset);
    int selectNoticesTotalPage(int perPage);
    int updateNoticeDetailHit(int noticeIndex);
    List<NoticeDto> selectNoticeDetailContent(int noticeIndex);
    int updateNoticeStatus(int noticeIndex);
    int insertNotice(NoticeDto noticeDto);
    int selectFirstNoticeNo();
    List<NoticeDto> selectNoticeDetailContentForTwoRow(int noticeIndex);
    int selectRecentNotice();
    NoticeDto selectNoticeDetailForDelete(int noticeIndex);
    List<NoticeDto> selectNoticeDetailForUserWithTwoRow(int noticeIndex);
    int selectNoBeforeIndex(int noticeIndex);

    int selectFirstNoticeNoWithStatus();
}
