package com.community.tool_library.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String role;
    private String lastLogin;
    private String createdAt;
    private String updatedAt;
}
