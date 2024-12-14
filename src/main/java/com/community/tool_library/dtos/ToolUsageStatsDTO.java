package com.community.tool_library.dtos;

public record ToolUsageStatsDTO(
        Long itemId,
        String name,
        Long ownerId,
        int borrowCount
) {}
