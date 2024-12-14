package com.community.tool_library.services;

import com.community.tool_library.dtos.ItemDTO;
import com.community.tool_library.models.Item;

import java.util.List;

public interface ItemService {
    ItemDTO createItem(ItemDTO itemDTO, Long ownerId);

    ItemDTO getItem(Long itemId);

    ItemDTO updateItem(ItemDTO itemDTO, long ownerId);

    void deleteItem(Long itemId, Long ownerId);

    List<ItemDTO> searchItems(String query);

    Item getItemEntity(Long itemId);
}
