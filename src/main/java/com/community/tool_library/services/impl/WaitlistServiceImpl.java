package com.community.tool_library.services.impl;

import com.community.tool_library.dtos.WaitlistEntryDTO;
import com.community.tool_library.models.Item;
import com.community.tool_library.models.User;
import com.community.tool_library.models.WaitlistEntry;
import com.community.tool_library.repositories.WaitlistEntryRepository;
import com.community.tool_library.services.ItemService;
import com.community.tool_library.services.NotificationService;
import com.community.tool_library.services.UserService;
import com.community.tool_library.services.WaitlistService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WaitlistServiceImpl implements WaitlistService {

    private final WaitlistEntryRepository waitlistEntryRepository;
    private final ItemService itemService;
    private final UserService userService;
    private final NotificationService notificationService;

    public WaitlistServiceImpl(WaitlistEntryRepository waitlistEntryRepository, ItemService itemService, UserService userService, NotificationService notificationService) {
        this.waitlistEntryRepository = waitlistEntryRepository;
        this.itemService = itemService;
        this.userService = userService;
        this.notificationService = notificationService;
    }

    @Override
    public void placeHold(Long itemId, Long userId) {
        // Validate item and user exist
        Item item = itemService.getItemEntity(itemId);
        User user = userService.getUserEntity(userId);

        // Check if user already has a hold on this item
        Optional<WaitlistEntry> existingHold = waitlistEntryRepository.findByItemIdAndUserId(itemId, userId);
        if (existingHold.isPresent()) {
            throw new RuntimeException("User already has a hold on this item.");
        }

        // Create waitlist entry
        WaitlistEntry waitlistEntry = WaitlistEntry.builder()
                .item(item)
                .user(user)
                .build();

        // Save waitlist entry
        WaitlistEntry saved = waitlistEntryRepository.save(waitlistEntry);
        mapToDTO(saved);
    }

    @Override
    public void cancelHold(Long itemId, Long userId) {
        // Check if user has a hold on this item
        WaitlistEntry entry = waitlistEntryRepository.findByItemIdAndUserId(itemId, userId)
                .orElseThrow(() -> new RuntimeException("User does not have a hold on this item."));

        waitlistEntryRepository.delete(entry);
        // TODO: Notify next user?
    }

    @Override
    public WaitlistEntryDTO notifyNextUser(Long itemId) {
        // Fetch the earliest waitlist entry for this item
        List<WaitlistEntry> waitlist = waitlistEntryRepository.findAllByItemIdOrderByCreatedAtAsc(itemId);

        // If no waitlist entries exist, return null
        if (waitlist.isEmpty()) {
            return null;
        }

        WaitlistEntry nextUser = waitlist.getFirst();
        String itemName = nextUser.getItem().getName();

        // create notification
        notificationService.createNotification(nextUser.getUser().getId(), "The item " + itemName + " is now available for you to borrow.");

        return mapToDTO(nextUser);
    }

    @Override
    public List<WaitlistEntryDTO> getWaitlistForItem(Long itemId) {
        List<WaitlistEntry> waitlist = waitlistEntryRepository.findAllByItemIdOrderByCreatedAtAsc(itemId);
        return waitlist.stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public List<Long> getWaitlistUserIdsByItem(Long itemId) {
        List<WaitlistEntry> waitlist = waitlistEntryRepository.findAllByItemIdOrderByCreatedAtAsc(itemId);
        return waitlist.stream()
                .map(WaitlistEntry::getUser)
                .map(User::getId)
                .toList();
    }

    @Override
    public List<Long> getWaitlistItemIdsByUser(Long userId) {
        List<WaitlistEntry> waitlist = waitlistEntryRepository.findAllByUserId(userId);
        return waitlist.stream()
                .map(WaitlistEntry::getItem)
                .map(Item::getId)
                .toList();
    }

    // helper methods
    private WaitlistEntryDTO mapToDTO(WaitlistEntry waitlistEntry) {
        return new WaitlistEntryDTO(
                waitlistEntry.getId(),
                waitlistEntry.getItem().getId(),
                waitlistEntry.getUser().getId(),
                waitlistEntry.getCreatedAt()
        );
    }
}
