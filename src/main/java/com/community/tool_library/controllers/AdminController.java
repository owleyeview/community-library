package com.community.tool_library.controllers;

import com.community.tool_library.dtos.*;
import com.community.tool_library.models.User;
import com.community.tool_library.services.ItemService;
import com.community.tool_library.services.LoanService;
import com.community.tool_library.services.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ItemService itemService;
    private final UserService userService;
    private final LoanService loanService;

    public AdminController(ItemService itemService, UserService userService, LoanService loanService) {
        this.itemService = itemService;
        this.userService = userService;
        this.loanService = loanService;
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
        List<AdminViewUserDTO> users;
        if (query != null && !query.trim().isEmpty()) {
            users = userService.searchUsers(query.trim());
        } else {
            users = userService.getAllUsers();
        }
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

    @GetMapping("/adminitems/new")
    public String newItemForm(
            @RequestParam(name="userId", required=false) Long userId,
            Model model
    ) {
        // if userId is present, store it
        NewItemDTO newItemDto = new NewItemDTO(null, null, null, userId);

        model.addAttribute("newItemDto", newItemDto);

        // This can be used if we want a hidden or read-only input for user ID
        // model.addAttribute("ownerId", userId);

        return "adminnewitem";
    }

    @PostMapping("/adminitems/new")
    public String createNewItem(
            @ModelAttribute("newItemDto") @Valid NewItemDTO newItemDto,
            BindingResult bindingResult,
            Model model
    ) {
        // if @Valid validation fails, reload the form with errors
        if (bindingResult.hasErrors()) {
            model.addAttribute("errorMessage",
                    "Validation failed: " + bindingResult.getAllErrors().getFirst().getDefaultMessage());
            return "adminnewitem";
        }

        // try/catch to handle exceptions from service
        try {
            itemService.createItem(newItemDto, newItemDto.ownerId());

            // show success message
            model.addAttribute("successMessage", "New item added successfully!");

            // reinitialize the form with a blank DTO for a new item
            NewItemDTO blankForm = new NewItemDTO(null, null, null, newItemDto.ownerId());
            model.addAttribute("newItemDto", blankForm);

            return "adminnewitem";
        } catch (Exception e) {
            // handle exceptions like invalid user ID or other errors
            model.addAttribute("errorMessage", "Could not create item: " + e.getMessage());
            model.addAttribute("newItemDto", newItemDto);
            return "adminnewitem";
        }
    }

    // Report methods
    @GetMapping("/adminusers/{id}/report")
    public String userLoanReport(@PathVariable Long id, Model model) {
        // fetch user info
        AdminViewUserDTO user = userService.getUserForAdmin(id);

        // fetch all loans for user
        List<LoanDTO> loans = loanService.getLoansByUser(id);

        // convert to LoanReportDTO, adding item name
        List<LoanReportDTO> loanReports = loans.stream()
                .map(loan -> {
                    ItemDTO item = itemService.getItem(loan.itemId());
                    return new LoanReportDTO(
                            loan.id(),
                            item.name(),
                            loan.createdAt(),
                            loan.updatedAt(),
                            loan.returned()
                    );
                }).toList();

        // current timestamp for display
        String timestamp = java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        model.addAttribute("user", user);
        model.addAttribute("loans", loanReports);
        model.addAttribute("timestamp", timestamp);

        return "adminuserreport";
    }

    @GetMapping("/adminitems/{id}/report")
    public String itemLoanReport(@PathVariable Long id, Model model) {
        // fetch item info for the name
        ItemDTO item = itemService.getItem(id);

        // fetch all loans for item
        List<LoanDTO> loans = loanService.getLoansByItem(id);

        // convert to LoanReportDTO, adding item name
        List<LoanReportDTO> loanReports = loans.stream()
                .map(loan -> {
                    UserDTO user = userService.getUser(loan.borrowerId());
                    return new LoanReportDTO(
                            loan.id(),
                            user.username(),
                            loan.createdAt(),
                            loan.updatedAt(),
                            loan.returned()
                    );
                }).toList();

        // current timestamp for display
        String timestamp = java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        model.addAttribute("item", item);
        model.addAttribute("loans", loanReports);
        model.addAttribute("timestamp", timestamp);

        return "adminitemreport";
    }


}
