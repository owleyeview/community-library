package com.community.tool_library.controllers;

import com.community.tool_library.dtos.ItemDTO;
import com.community.tool_library.dtos.LoanDTO;
import com.community.tool_library.dtos.UserDTO;
import com.community.tool_library.services.ItemService;
import com.community.tool_library.services.LoanService;
import com.community.tool_library.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class UserController {
    private final UserService userService;
    private final ItemService itemService;
    private final LoanService loanService;

    public UserController(UserService userService, ItemService itemService, LoanService loanService) {
        this.userService = userService;
        this.itemService = itemService;
        this.loanService = loanService;
    }

    @GetMapping("/profile")
    public String profile(Principal principal, Model model) {
        UserDTO user = userService.getUser(principal.getName());
        List<ItemDTO> ownedItems = itemService.getItemsByOwnerId(user.id());
        List<LoanDTO> borrowedItems = loanService.getActiveLoansByUser(user.id());
        model.addAttribute("user", user);
        model.addAttribute("ownedItems", ownedItems);
        model.addAttribute("borrowedItems", borrowedItems);
        return "profile";
    }
}
