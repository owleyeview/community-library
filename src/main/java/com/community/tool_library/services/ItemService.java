package com.community.tool_library.services;

import com.community.tool_library.dtos.ItemDTO;
import com.community.tool_library.models.Item;

import java.util.List;

public interface ItemService {
    ItemDTO createItem(ItemDTO itemDTO, Long userId);

    ItemDTO getItem(Long itemId);

    ItemDTO updateItem(ItemDTO itemDTO, long userId);

    void updateAvailability(Long itemId, boolean available);

    void deleteItem(Long itemId, Long userId);

    List<ItemDTO> searchItems(String query);

    Item getItemEntity(Long itemId);
}
