package com.community.tool_library.dtos;

import java.time.LocalDateTime;

public record AdminViewUserDTO(
        Long id,
        String username,
        String email,
        String role,
        LocalDateTime lastLogin,
        LocalDateTime createdAt
) {}
