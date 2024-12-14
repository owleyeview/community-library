package com.community.tool_library.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ToolDTO {
    private Long id;
    private String name;
    private String description;
    private boolean availability;
    private String value;
    private UserDTO owner;
}
