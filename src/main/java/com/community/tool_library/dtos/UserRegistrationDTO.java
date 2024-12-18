package com.community.tool_library.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRegistrationDTO(
        @NotBlank
        String username,

        @NotBlank
        @Email
        String email,

        @NotBlank
        @Size(min = 8)
        String password // raw password!
) {}
