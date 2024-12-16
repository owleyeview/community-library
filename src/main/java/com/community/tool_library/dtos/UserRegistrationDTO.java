package com.community.tool_library.dtos;

public record UserRegistrationDTO(
        String username,
        String email,
        String password // raw password!
) {}
