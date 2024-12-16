package com.community.tool_library.repositories;

import com.community.tool_library.models.WaitlistEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WaitlistEntryRepository extends JpaRepository<WaitlistEntry, Long> {

    List<WaitlistEntry> findAllByItemIdOrderByCreatedAtAsc(Long itemId);

    Optional<WaitlistEntry> findByItemIdAndUserId(Long itemId, Long userId);
}
