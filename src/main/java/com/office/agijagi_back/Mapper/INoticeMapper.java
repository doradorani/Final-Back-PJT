package com.office.agijagi_back.Mapper;

import com.office.agijagi_back.Dto.NoticeDto;
import com.office.agijagi_back.Dto.PostDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface INoticeMapper {
    List<NoticeDto> selectNoticeTableList(int perPage, int offset);
    int selectNoticeTotalPage(int perPage);
}
