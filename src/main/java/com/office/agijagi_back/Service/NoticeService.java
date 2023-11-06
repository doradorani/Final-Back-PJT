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

        if (offset >= 10) {
            totalPages = noticeMapper.selectNoticeTotalPage(perPage);
        }

        Map<String, Object> NoticeMap = new HashMap<>();
        NoticeMap.put("noticeDtos", noticeDtos);
        NoticeMap.put("totalPages", totalPages);

        return NoticeMap;
    }

    public NoticeDto getNoticeDetail(int noticeIndex, int modifyRequest) {
        log.info("[NoticeService] getNoticeDetail");

        if (modifyRequest == 0) {
            int result = noticeMapper.updateNoticeDetailHit(noticeIndex);
            if (result != 0) {
                NoticeDto noticeDto = noticeMapper.selectNoticeDetailContent(noticeIndex);
                return noticeDto;
            }
        } else if (modifyRequest == 1) {
            NoticeDto noticeDto = noticeMapper.selectNoticeDetailContent(noticeIndex);
            return noticeDto;
        }
        return null;
    }

    public int deleteNotice(int noticeIndex) {
        log.info("[NoticeService] deleteNotice");

        NoticeDto noticeDto = noticeMapper.selectNoticeDetailContent(noticeIndex);
        int noticeStatus = noticeDto.getStatus();
        int result = 0;
        if (noticeStatus == 1) {
            result = noticeMapper.updateNoticeStatus(noticeIndex);
            return result;
        }
        return result;
    }
}
