package com.community.tool_library.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WaitlistEntryDTO {
    private Long id;
    private Long itemId;
    private Long userId;
    private String createdAt;
}
