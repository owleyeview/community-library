package com.community.tool_library.dtos;

import java.time.LocalDateTime;

public record LoanReportDTO(
        Long id,
        String displayValue, // either itemName or borrowerUsername, depending on context
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        boolean returned
) {}

