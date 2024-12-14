package com.community.tool_library.dtos;

import java.time.LocalDateTime;

public record LoanDTO (
     Long id,
     Long itemId,
     Long borrowerId,
     boolean returned,
     LocalDateTime createdAt,
     LocalDateTime updatedAt
) {}
