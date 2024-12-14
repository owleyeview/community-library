package com.community.tool_library.dtos;

import java.time.LocalDateTime;

public record WaitlistEntryDTO(
        Long id,
        Long itemId,
        Long userId,
        LocalDateTime createdAt
) {}
