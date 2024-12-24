package com.community.tool_library.services.impl;

import com.community.tool_library.dtos.ItemDTO;
import com.community.tool_library.models.Item;
import com.community.tool_library.models.Tool;
import com.community.tool_library.models.User;
import com.community.tool_library.repositories.ItemRepository;
import com.community.tool_library.repositories.UserRepository;
import com.community.tool_library.services.ItemService;
import com.community.tool_library.services.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final UserService userService;

    public ItemServiceImpl(UserRepository userRepository, ItemRepository itemRepository, UserService userService) {
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
        this.userService = userService;
    }

    @Override
    public ItemDTO createItem(ItemDTO itemDTO, Long userId) {
        User owner = userService.getUserEntity(userId);
        Item item = mapToEntity(itemDTO, owner);
        Item savedItem = itemRepository.save(item);
        return mapToDTO(savedItem);
    }

    @Override
    public ItemDTO getItem(Long itemId) {
        Item item = getItemEntity(itemId);
        return mapToDTO(item);
    }

    @Override
    public ItemDTO updateItem(ItemDTO itemDTO, long userId) {
        Item item = getItemEntity(itemDTO.id());
        validateOwnership(item, userId);
        item.setName(itemDTO.name());
        item.setDescription(itemDTO.description());
        item.setAvailable(itemDTO.available());
        item.setValue(itemDTO.value());
        Item updatedItem = itemRepository.save(item);
        return mapToDTO(updatedItem);
    }

    @Override
    public void updateAvailability(Long itemId, boolean available) {
        Item item = getItemEntity(itemId);
        item.setAvailable(available);
        itemRepository.save(item);
    }

    @Override
    public void deleteItem(Long itemId, Long userId) {
        Item item = getItemEntity(itemId);
        validateOwnership(item, userId);
        itemRepository.delete(item);
    }

    @Override
    public List<ItemDTO> searchItems(String query) {
        return itemRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(query, query).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

//  other possible search methods
//    @Override
//    public List<ItemDTO> searchItemNames(String nameQuery) {
//        return itemRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(nameQuery, "").stream()
//                .map(this::mapToDTO)
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public List<ItemDTO> searchItemDescriptions(String descriptionQuery) {
//        return itemRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase("", descriptionQuery).stream()
//                .map(this::mapToDTO)
//                .collect(Collectors.toList());
//    }

    @Override
    public Item getItemEntity(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found with ID: " + itemId));
    }

    // helper methods
    private Item mapToEntity(ItemDTO dto, User owner) {
        return Tool.toolBuilder()
                .name(dto.name())
                .description(dto.description())
                .available(dto.available())
                .value(dto.value())
                .owner(owner)
                .build();
    }

    private ItemDTO mapToDTO(Item item) {
        return new ItemDTO(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.isAvailable(),
                item.getValue(),
                item.getOwner().getId()
        );
    }

    private void validateOwnership(Item item, Long userId) {
        if (!item.getOwner().getId().equals(userId)
                && SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            throw new RuntimeException("Only the owner or an admin can perform this action.");
        }
    }

}
