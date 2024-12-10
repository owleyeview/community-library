package com.community.tool_library.models;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("ADMIN")
@Getter
@Setter
@NoArgsConstructor
public class AdminUser extends User{

    @Builder(builderMethodName = "adminBuilder")
    public AdminUser(Long id, String username, String password, String email, String role, LocalDateTime lastLogin, LocalDateTime createdAt, LocalDateTime updatedAt) {
        super(id, username, password, email, role, lastLogin, createdAt, updatedAt);
    }
}
