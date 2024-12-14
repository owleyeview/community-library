package com.community.tool_library.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "WAITLIST_ENTRY")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WaitlistEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "item_id", nullable = false)
    @NotBlank
    private Item item;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @NotBlank
    private User user;

    @Column(nullable = false, updatable = false)
    @NotBlank
    @CreationTimestamp
    private LocalDateTime createdAt;
}
