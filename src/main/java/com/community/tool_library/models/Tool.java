package com.community.tool_library.models;


import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("TOOL")
@Getter
@Setter
@NoArgsConstructor
public class Tool extends Item {

    @Builder(builderMethodName = "toolBuilder")
    public Tool(Long id, String name, String description, boolean available, BigDecimal value, User owner,
                LocalDateTime createdAt, LocalDateTime updatedAt) {
        super(id, name, description, available, value, owner, createdAt, updatedAt);
    }
}
