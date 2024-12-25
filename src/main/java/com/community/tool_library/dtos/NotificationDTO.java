package com.community.tool_library.dtos;

import jdk.jshell.Snippet;

import java.time.LocalDateTime;

public record NotificationDTO(
        Long id,
        Long recipientId,
        String message,
        boolean read,
        LocalDateTime createdAt
) {
}
