package com.community.tool_library.dtos;

public record ItemUsageStatsDTO(
        Long itemId,
        String name,
        Long ownerId,
        int borrowCount
) {}
