package com.community.tool_library.services;

import com.community.tool_library.dtos.ItemDTO;
import com.community.tool_library.dtos.NewItemDTO;
import com.community.tool_library.models.Item;

import java.util.List;

public interface ItemService {
    void createItem(NewItemDTO newItemDTO, Long userId);

    ItemDTO getItem(Long itemId);

    List<ItemDTO> getItemsByOwnerId(Long id);

    void updateItem(ItemDTO itemDTO, long userId);

    void updateAvailability(Long itemId, boolean available);

    void deleteItem(Long itemId, Long userId);

    List<ItemDTO> searchItems(String query);

    Item getItemEntity(Long itemId);


}
