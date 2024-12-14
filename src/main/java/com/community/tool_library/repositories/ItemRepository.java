package com.community.tool_library.repositories;

import com.community.tool_library.models.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    //  Custom queries here
    <List>Item findDistinctByNameOrDescriptionOrderByName(String name, String description);
}
