package com.community.tool_library.controllers;

import com.community.tool_library.dtos.AdminViewUserDTO;
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
        AdminViewUserDTO adminUserDetail = userService.getUserForAdmin(id);
        model.addAttribute("user", adminUserDetail);
        return "adminuserdetail";
    }
    // User methods
    @GetMapping("/adminusers/{id}/edit")
    public String editUserForm(@PathVariable Long id, Model model) {
        AdminViewUserDTO userDto = userService.getUserForAdmin(id);
        model.addAttribute("user", userDto);
        return "adminuseredit";
    }

    @PostMapping("/adminusers/{id}/edit")
    public String updateUser(
            @PathVariable Long id,
            @ModelAttribute AdminViewUserDTO userDto
    ) {
        userService.adminUpdateUser(userDto);
        return "redirect:/admin/adminusers/" + id;
    }

    @PostMapping("/adminusers/{id}/delete")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/admin/adminusers";
    }

    // Item methods
    @GetMapping("/adminitems/{id}/edit")
    public String editItemForm(@PathVariable Long id, Model model) {
        ItemDTO item = itemService.getItem(id);
        model.addAttribute("item", item);
        return "adminitemedit";
    }

    @PostMapping("/adminitems/{id}/edit")
    public String updateItem(
            @PathVariable Long id,
            @ModelAttribute ItemDTO item
    ) {
        // could ensure that ownerId is not changed here by setting it to the original value

        // update item; passing a dummy userId for admin
        itemService.updateItem(item, 0L);
        return "redirect:/admin/adminitems/" + id;
    }

    @PostMapping("/adminitems/{id}/delete")
    public String deleteItem(@PathVariable Long id) {
        // no need to pass userId; admin will be validated in service
        itemService.deleteItem(id, 0L);
        return "redirect:/admin/adminitems";
    }
}
