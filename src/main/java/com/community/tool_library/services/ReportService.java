package com.community.tool_library.services;

import com.community.tool_library.dtos.ItemUsageStatsDTO;
import com.community.tool_library.dtos.UserBorrowingStatsDTO;

import java.util.List;

public interface ReportService {

    List<ItemUsageStatsDTO> getMostBorrowedTools();

    List<UserBorrowingStatsDTO> getUserBorrowingReport();
}
