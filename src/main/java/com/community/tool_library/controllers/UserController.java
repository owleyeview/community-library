package com.community.tool_library.controllers;

import com.community.tool_library.dtos.ItemDTO;
import com.community.tool_library.dtos.ItemStatusDTO;
import com.community.tool_library.dtos.LoanDTO;
import com.community.tool_library.dtos.UserDTO;
import com.community.tool_library.services.ItemService;
import com.community.tool_library.services.LoanService;
import com.community.tool_library.services.UserService;
import com.community.tool_library.services.WaitlistService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class UserController {
    private final UserService userService;
    private final ItemService itemService;
    private final LoanService loanService;
    private final WaitlistService waitlistService;

    public UserController(UserService userService, ItemService itemService, LoanService loanService, WaitlistService waitlistService) {
        this.userService = userService;
        this.itemService = itemService;
        this.loanService = loanService;
        this.waitlistService = waitlistService;
    }

    @GetMapping("/profile")
    public String profile(Principal principal, Model model) {
        Long userId = userService.getUserId(principal.getName());
        UserDTO user = userService.getUser(userId);

        // fetch user's owned items
        List<ItemDTO> ownedItems = itemService.getItemsByOwnerId(user.id());

        // fetch user's active loans
        List<Long> activeLoanItemIds = loanService.getActiveLoanItemIdsByUser(userId);

        List<ItemDTO> borrowedItems = new ArrayList<>();
        for (Long itemId : activeLoanItemIds) {
            borrowedItems.add(itemService.getItem(itemId));
        }

        // waitlist items
        Set<Long> userWaitlistItems = new HashSet<>(waitlistService.getWaitlistItemIdsByUser(userId));

        // convert lists to ItemStatusDTOs
        List<ItemStatusDTO> enrichedOwnedItems = new ArrayList<>();
        for (ItemDTO item : ownedItems) {
            boolean userHasIt = activeLoanItemIds.contains(item.id());
            boolean userOnWaitlist = userWaitlistItems.contains(item.id());
            enrichedOwnedItems.add(addStatusFields(item, userHasIt, userOnWaitlist));
        }

        List<ItemStatusDTO> enrichedBorrowedItems = new ArrayList<>();
        for (ItemDTO item : borrowedItems) {
            boolean userHasIt = true; // we know user borrowed it
            boolean userOnWaitlist = userWaitlistItems.contains(item.id());
            enrichedBorrowedItems.add(addStatusFields(item, userHasIt, userOnWaitlist));
        }

        model.addAttribute("user", user);
        model.addAttribute("ownedItems", enrichedOwnedItems);
        model.addAttribute("borrowedItems", enrichedBorrowedItems);

        return "profile";
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
