package com.community.tool_library.services;

import com.community.tool_library.dtos.ToolUsageStatsDTO;
import com.community.tool_library.dtos.UserBorrowingStatsDTO;

import java.util.List;

public interface ReportService {

    List<ToolUsageStatsDTO> getMostBorrowedTools();

    List<UserBorrowingStatsDTO> getUserBorrowingReport();
}
