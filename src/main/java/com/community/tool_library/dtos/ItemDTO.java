package com.community.tool_library.dtos;

import java.math.BigDecimal;

public record ItemDTO (
     Long id,
     String name,
     String description,
     boolean available,
     BigDecimal value,
     Long ownerId
) {}
