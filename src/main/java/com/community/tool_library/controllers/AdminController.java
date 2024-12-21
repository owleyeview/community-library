package com.community.tool_library.controllers;

import com.community.tool_library.dtos.AdminUserDetailDTO;
import com.community.tool_library.dtos.ItemDTO;
import com.community.tool_library.dtos.UserDTO;
import com.community.tool_library.models.User;
import com.community.tool_library.services.ItemService;
import com.community.tool_library.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ItemService itemService;
    private final UserService userService;

    public AdminController(ItemService itemService, UserService userService) {
        this.itemService = itemService;
        this.userService = userService;
    }

    @GetMapping
    public String adminHome() {
        // Shows the admin.html landing page
        return "admin";
    }

    @GetMapping("/adminitems")
    public String adminItems(@RequestParam(name="q", required=false) String query, Model model) {
        List<ItemDTO> items;
        if (query != null && !query.trim().isEmpty()) {
            items = itemService.searchItems(query.trim());
        } else {
            items = itemService.searchItems("");
        }
        model.addAttribute("itemlist", items);
        return "adminitems";
    }

    @GetMapping("/adminitems/{id}")
    public String adminItemDetail(@PathVariable Long id, Model model) {
        ItemDTO item = itemService.getItem(id);
        model.addAttribute("item", item);
        return "adminitemdetail";
    }

    @GetMapping("/adminusers")
    public String adminUsers(@RequestParam(name="q", required=false) String query, Model model) {
        List<UserDTO> users;
        //if (query != null && !query.trim().isEmpty()) {
        //    users = userService.searchUsers(query.trim());
        //} else {
            users = userService.getAllUsers();
        //}
        model.addAttribute("userlist", users);
        return "adminusers";
    }

    @GetMapping("/adminusers/{id}")
    public String adminUserDetail(@PathVariable Long id, Model model) {
        // Fetch user entity
        User user = userService.getUserEntity(id);
        // Convert to AdminUserDetailDTO
        AdminUserDetailDTO adminUserDetail = new AdminUserDetailDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.getLastLogin(),
                user.getCreatedAt()
        );
        model.addAttribute("user", adminUserDetail);
        return "adminuserdetail";
    }
}
