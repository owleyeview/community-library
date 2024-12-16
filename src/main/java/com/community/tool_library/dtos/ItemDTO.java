package com.community.tool_library.dtos;

public record ItemDTO (
     Long id,
     String name,
     String description,
     boolean available,
     String value,
     Long ownerId
) {}
