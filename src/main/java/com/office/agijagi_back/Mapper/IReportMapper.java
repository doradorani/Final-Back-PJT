package com.office.agijagi_back.Mapper;

import com.office.agijagi_back.Dto.PostReportDto;
import com.office.agijagi_back.Dto.ReplyReportDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IReportMapper {
    List<PostReportDto> selectPostReportTable(int perPage, int offset);

    int selectPostReportTotalPage(int perPage);

    List<ReplyReportDto> selectReplyReportTable(int perPage, int offset);

    int selectReplyReportTotalPage(int perPage);

    int updatePostReportStatusForDelete(int reportIndex);

    int updateReplyReportStatusForDelete(int reportIndex);
}
