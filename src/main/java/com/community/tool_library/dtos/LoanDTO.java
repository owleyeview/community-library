package com.community.tool_library.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoanDTO {
    private Long id;
    private Long itemId;
    private Long borrowerId;
    private boolean returned;
    private String createdAt;
    private String updatedAt;
}
