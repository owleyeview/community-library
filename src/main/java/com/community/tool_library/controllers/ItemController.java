package com.community.tool_library.controllers;

import com.community.tool_library.dtos.ItemDTO;
import com.community.tool_library.dtos.ItemStatusDTO;
import com.community.tool_library.dtos.LoanDTO;
import com.community.tool_library.models.Item;
import com.community.tool_library.models.WaitlistEntry;
import com.community.tool_library.services.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class ItemController {

    private final ItemService itemService;
    private final LoanService loanService;
    private final WaitlistService waitlistService;
    private final UserService userService;
    private final NotificationService notificationService;

    public ItemController(ItemService itemService, LoanService loanService, WaitlistService waitlistService, UserService userService, NotificationService notificationService) {
        this.itemService = itemService;
        this.loanService = loanService;
        this.waitlistService = waitlistService;
        this.userService = userService;
        this.notificationService = notificationService;
    }

    @GetMapping("/items")
    public String listItems(@RequestParam(name="q", required=false) String query,
                            Principal principal,
                            Model model
    ) {
        Long currentUserId = userService.getUserId(principal.getName());

        List<ItemDTO> items;
        if (query != null && !query.trim().isEmpty()) {
            items = itemService.searchItems(query.trim());
        } else {
            items = itemService.searchItems("");
        }

        Set<Long> userActiveLoans = new HashSet<> (loanService.getActiveLoanItemIdsByUser(currentUserId));
        Set<Long> userWaitlistItems = new HashSet<> (waitlistService.getWaitlistItemIdsByUser(currentUserId));

        List<ItemStatusDTO> enrichedItems = new ArrayList<>();

        for (ItemDTO item : items) {
            boolean userHasIt = userActiveLoans.contains(item.id());
            boolean userOnWaitlist = userWaitlistItems.contains(item.id());

            enrichedItems.add(addStatusFields(item, userHasIt, userOnWaitlist));
        }

        // count unread notifications
        long unreadCount = notificationService.countUnreadNotifications(currentUserId);

        model.addAttribute("unreadNotifications", unreadCount);
        model.addAttribute("itemlist", enrichedItems);
        return "items";
    }

    @GetMapping("/items/{id}")
    public String itemDetails(@PathVariable Long id, Principal principal, Model model) {
        Long currentUserId = userService.getUserId(principal.getName());

        ItemDTO item = itemService.getItem(id);

        boolean borrowedByCurrentUser = loanService.getActiveLoanItemIdsByUser(currentUserId).contains(id);
        boolean currentUserInWaitlist = waitlistService.getWaitlistItemIdsByUser(currentUserId).contains(id);

        ItemStatusDTO enrichedItem = addStatusFields(item, borrowedByCurrentUser, currentUserInWaitlist);

        model.addAttribute("item", enrichedItem);
        return "itemdetail";
    }

    @PostMapping("/items/{id}/borrow")
    public String borrowItem(@PathVariable Long id, Principal principal) {
        Long userId = userService.getUserId(principal.getName());
        loanService.borrowItem(id, userId);
        return "redirect:/items";
    }

    @PostMapping("/items/{id}/return")
    public String returnItem(@PathVariable Long id, Principal principal) {
        Long userId = userService.getUserId(principal.getName());
        loanService.returnItem(id, userId);
        return "redirect:/items";
    }

    @PostMapping("/items/{id}/hold")
    public String placeHold(@PathVariable Long id, Principal principal) {
        Long userId = userService.getUserId(principal.getName());
        waitlistService.placeHold(id, userId);
        return "redirect:/items";
    }

    @PostMapping("/items/{id}/hold/cancel")
    public String cancelHold(@PathVariable Long id, Principal principal) {
        Long userId = userService.getUserId(principal.getName());
        waitlistService.cancelHold(id, userId);
        return "redirect:/items";
    }

    // helper methods
    private ItemStatusDTO addStatusFields(ItemDTO item, boolean userHasIt, boolean userOnWaitlist) {
        return new ItemStatusDTO(item.id(),
                item.name(),
                item.description(),
                item.available(),
                item.value(),
                item.ownerId(),
                userHasIt,
                userOnWaitlist
        );
    }
}
