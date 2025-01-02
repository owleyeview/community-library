package com.community.tool_library.dtos;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record NewItemDTO(
        @NotBlank(message = "Name cannot be blank")
        String name,

        String description,

        @DecimalMin(value = "0.0", message = "Value cannot be negative")
        BigDecimal value,

        Long ownerId
) {
}
