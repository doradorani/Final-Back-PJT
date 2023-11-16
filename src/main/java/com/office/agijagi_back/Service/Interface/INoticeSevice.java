package com.office.agijagi_back.Service.Interface;

import com.office.agijagi_back.Dto.NoticeDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface INoticeSevice {
    Map<String, Object> getNoticeTable(int currentPage, int perPage);
    Map<String, Object> getNotices(int currentPage, int perPage);
    List<NoticeDto> getNoticeDetail(int noticeIndex, int modifyRequest);
    Map<String, Object> getNoticeDetailForUser(int noticeIndex);
    int deleteNotice(int noticeIndex) throws Exception;
    int registNotice(String adminId, Map<String, String> data, List<MultipartFile> files, List<String> fileList) throws IOException;
    int getRecentNotice();
    int modifyNotice(String adminId, Map<String, Object> data, List<MultipartFile> files, List<String> fileList);

}
