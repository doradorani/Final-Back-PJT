package com.office.agijagi_back.Service;

import com.office.agijagi_back.Dto.NoticeDto;
import com.office.agijagi_back.Mapper.INoticeMapper;
import com.office.agijagi_back.Util.S3.S3Service;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@Service
public class NoticeService {

    private final INoticeMapper noticeMapper;
    private final S3Service s3Service;

    public NoticeService(INoticeMapper noticeMapper, S3Service s3Service) {
        this.noticeMapper = noticeMapper;
        this.s3Service = s3Service;;
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

    public Map<String, Object> getNotices(int currentPage, int perPage) {
        log.info("[NoticeService] getNotices");

        int offset = (currentPage - 1) * perPage;

        List<NoticeDto> noticeDtos = noticeMapper.selectNotices(perPage, offset);
        int totalPages = noticeMapper.selectNoticesTotalPage(perPage);;

        Map<String, Object> NoticeMap = new HashMap<>();
        NoticeMap.put("noticeDtos", noticeDtos);
        NoticeMap.put("totalPages", totalPages);

        return NoticeMap;
    }

    public List<NoticeDto> getNoticeDetail(int noticeIndex, int modifyRequest) {
        log.info("[NoticeService] getNoticeDetail");

        if (modifyRequest == 0) {
            int result = noticeMapper.updateNoticeDetailHit(noticeIndex);
            int firstNoticeIndex = noticeMapper.selectFirstNoticeNo();
            if (result != 0 && firstNoticeIndex != noticeIndex) {
                List<NoticeDto> noticeDtos = noticeMapper.selectNoticeDetailContent(noticeIndex);
                return noticeDtos;
            } else {
                List<NoticeDto> noticeDtos = noticeMapper.selectNoticeDetailContentForTwoRow(noticeIndex);

                return noticeDtos;
            }
        } else if (modifyRequest == 1) {
            List<NoticeDto> noticeDtos = noticeMapper.selectNoticeDetailContent(noticeIndex);
            return noticeDtos;
        }
        return null;
    }

    public Map<String, Object> getNoticeDetailForUser(int noticeIndex) {
        log.info("[NoticeService] getNoticeDetailForUser");

        Map<String, Object> noticeDtosMap = new HashMap<>();
        int result = noticeMapper.updateNoticeDetailHit(noticeIndex);
        int firstNoticeIndex = noticeMapper.selectFirstNoticeNoWithStatus();
        if (result != 0 && firstNoticeIndex != noticeIndex) {
            int noBeforeIndex = noticeMapper.selectNoBeforeIndex(noticeIndex);
            NoticeDto noticeDto = noticeMapper.selectNoticeDetailForDelete(noBeforeIndex);
            List<NoticeDto> noticeDtos = noticeMapper.selectNoticeDetailForUserWithTwoRow(noticeIndex);
            noticeDtosMap.put("data1",noticeDto);
            noticeDtosMap.put("data2",noticeDtos);

            return noticeDtosMap;
        } else if (result != 0 && firstNoticeIndex == noticeIndex) {
            List<NoticeDto> noticeDtos = noticeMapper.selectNoticeDetailForUserWithTwoRow(noticeIndex);
            noticeDtosMap.put("data1",noticeDtos);

            return noticeDtosMap;
        }
        return null;
    };


    public int deleteNotice(int noticeIndex) throws Exception {
        log.info("[NoticeService] deleteNotice");

        NoticeDto noticeDto = noticeMapper.selectNoticeDetailForDelete(noticeIndex);
        int noticeStatus = noticeDto.getStatus();
        int attachCnt = noticeDto.getAttach_cnt();
        String attachPath = noticeDto.getAttach_path();
        int result = 0;
        if (noticeStatus == 1) {
            if (attachPath != null) {
                String[] attachPathList = attachPath.split(",");
                for (int i = 0 ; i < attachCnt ; i++ ) {
                    s3Service.deleteFile(attachPathList[i]);
                }
            }
            result = noticeMapper.updateNoticeStatus(noticeIndex);
            return result;
        }
        return result;
    }

    public int registNotice(String adminId, Map<String, String> data, List<MultipartFile> files, List<String> fileList) throws IOException {
        log.info("[NoticeService] registNotice");

        int result = -1;
        NoticeDto noticeDto = new NoticeDto();
        noticeDto.setAdmin_id(adminId);
        noticeDto.setTitle(data.get("title"));
        noticeDto.setContent(data.get("content"));
        noticeDto.setAttach_cnt(Integer.parseInt(data.get("uploadFile_cnt")));
        if (fileList == null) {
            result = noticeMapper.insertNotice(noticeDto);
        } else {
            String attachFilePath = "";
            String fileName = "";
            for (int i = 0; i < fileList.size() ; i++) {
                if (i == 0) {
                    attachFilePath = fileList.get(i);
                    fileName = files.get(i).getOriginalFilename();
                } else {
                    attachFilePath = attachFilePath + "," + fileList.get(i);
                    fileName = fileName + "," + files.get(i).getOriginalFilename();
                }
            }
            noticeDto.setFile_name(fileName);
            noticeDto.setAttach_path(attachFilePath);
            result = noticeMapper.insertNotice(noticeDto);
        }
        return result;
    }

    public int getRecentNotice() {
        log.info("[NoticeService] registNotice");

        int recentNoticeIndex = noticeMapper.selectRecentNotice();
        return recentNoticeIndex;
    }

    public int modifyNotice(String adminId, Map<String, String> data, List<MultipartFile> files, List<String> fileList) {
        log.info("[NoticeService] modifyNotice");

        int result = -1;
        NoticeDto noticeDto = new NoticeDto();
        noticeDto.setAdmin_id(adminId);
        noticeDto.setTitle(data.get("title"));
        noticeDto.setContent(data.get("content"));
        noticeDto.setAttach_cnt(Integer.parseInt(data.get("uploadFile_cnt")));
        if (fileList == null) {
            result = noticeMapper.insertNotice(noticeDto);
        } else {
            String attachFilePath = "";
            String fileName = "";
            for (int i = 0; i < fileList.size() ; i++) {
                if (i == 0) {
                    attachFilePath = fileList.get(i);
                    fileName = files.get(i).getOriginalFilename();
                } else {
                    attachFilePath = attachFilePath + "," + fileList.get(i);
                    fileName = fileName + "," + files.get(i).getOriginalFilename();
                }
            }
            noticeDto.setFile_name(fileName);
            noticeDto.setAttach_path(attachFilePath);
            result = noticeMapper.insertNotice(noticeDto);
        }
        return result;
    }



}
