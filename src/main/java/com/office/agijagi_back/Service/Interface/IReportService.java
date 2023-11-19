package com.office.agijagi_back.Service.Interface;

import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public interface IReportService {

    Map<String, Object> getPostReports(int currentPage, int perPage);

    Map<String, Object> getReplyReports(int currentPage, int perPage);

    int deletePostReport(int postIndex, int reportIndex);

    int rejectPostReport(int reportIndex);

    int deleteReplyReport(int postIndex, int replyIndex, int reportIndex);

    int rejectReplyReport(int reportIndex);
}
