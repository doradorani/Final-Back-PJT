package com.office.agijagi_back.Service;

import com.office.agijagi_back.Dto.NoticeDto;
import com.office.agijagi_back.Dto.PostDto;
import com.office.agijagi_back.Mapper.INoticeMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@Service
public class NoticeService {

    private final INoticeMapper noticeMapper;

    public NoticeService(INoticeMapper noticeMapper) {
        this.noticeMapper = noticeMapper;
    }



    public Map<String, Object> getNoticeTable(int currentPage, int perPage) {
        log.info("[NoticeService] getNoticeTable");

        int offset = (currentPage - 1) * perPage;

        List<NoticeDto> noticeDtos = noticeMapper.selectNoticeTableList(perPage, offset);
        int totalPages = noticeMapper.selectNoticeTotalPage(perPage);

        Map<String, Object> NoticeMap = new HashMap<>();
        NoticeMap.put("noticeDtos", noticeDtos);
        NoticeMap.put("totalPages", totalPages);

        return NoticeMap;
    }
}
