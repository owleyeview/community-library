package com.community.tool_library.dtos;

public record UserBorrowingStatsDTO(
        Long userID,
        String username,
        int totalBorrows,
        int activeBorrows
) {}
