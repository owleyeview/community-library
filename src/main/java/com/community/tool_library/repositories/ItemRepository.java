package com.community.tool_library.repositories;

import com.community.tool_library.models.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    // -- Custom queries --
    // Search items by name or description (case-insensitive)
    List<Item> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String nameQuery, String descriptionQuery);

    // Find all items owned by a specific user
    List<Item> findByOwnerId(Long ownerId);

    // Find all items that are available for borrowing
    List<Item> findByAvailableTrue();
}
