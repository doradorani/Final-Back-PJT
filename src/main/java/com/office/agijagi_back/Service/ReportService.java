package com.office.agijagi_back.Service;

import com.office.agijagi_back.Dto.PostReportDto;
import com.office.agijagi_back.Dto.ReplyReportDto;
import com.office.agijagi_back.Mapper.ICommunityMapper;
import com.office.agijagi_back.Mapper.IReportMapper;
import com.office.agijagi_back.Mapper.IUserMapper;
import com.office.agijagi_back.Service.Interface.IReportService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@Service
public class ReportService implements IReportService {

    private final IReportMapper reportMapper;
    private final ICommunityMapper communityMapper;
    private final IUserMapper userMapper;

    public ReportService(IReportMapper reportMapper,ICommunityMapper communityMapper,  IUserMapper userMapper) {
        this.reportMapper = reportMapper;
        this.communityMapper = communityMapper;
        this.userMapper= userMapper;
    }

    public Map<String, Object> getPostReports(int currentPage, int perPage) {
        log.info("[ReportService] getPostReports");

        int offset = (currentPage - 1) * perPage;
        List<PostReportDto> postReportDtos = reportMapper.selectPostReportTable(perPage, offset);
        int totalPages = reportMapper.selectPostReportTotalPage(perPage);

        Map<String, Object> PostReportMap = new HashMap<>();
        PostReportMap.put("postReportDtos", postReportDtos);
        PostReportMap.put("totalPages", totalPages);

        return PostReportMap;
    }

    public Map<String, Object> getReplyReports(int currentPage, int perPage) {
        log.info("[ReportService] getReplyReports");

        int offset = (currentPage - 1) * perPage;
        List<ReplyReportDto> replyReportDtos = reportMapper.selectReplyReportTable(perPage, offset);
        int totalPages = reportMapper.selectReplyReportTotalPage(perPage);

        Map<String, Object> ReplyReportMap = new HashMap<>();
        ReplyReportMap.put("replyReportDtos", replyReportDtos);
        ReplyReportMap.put("totalPages", totalPages);

        return ReplyReportMap;
    }

    public int deletePostReport(int postIndex, int reportIndex) {
        log.info("[ReportService] deletePostReport");

        int result = communityMapper.updatePostForDelete(postIndex);
        if (result != 0) {
            return reportMapper.updatePostReportStatusForDelete(reportIndex);
        }

        return result;
    }
}
