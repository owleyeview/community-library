package com.community.tool_library.controllers;

import com.community.tool_library.dtos.NotificationDTO;
import com.community.tool_library.services.NotificationService;
import com.community.tool_library.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class NotificationController {

    private final NotificationService notificationService;
    private final UserService userService;

    public NotificationController(NotificationService notificationService, UserService userService) {
        this.notificationService = notificationService;
        this.userService = userService;
    }

    @GetMapping("/notifications")
    public String getUserNotifications(Principal principal, Model model) {
        Long userId = userService.getUserId(principal.getName());

        // fetch user's notifications
        List<NotificationDTO> notifications = notificationService.getUserNotifications(userId);
        model.addAttribute("notifications", notifications);

        // mark all notifications as read
        notificationService.markAllAsRead(userId);

        return "notifications";
    }
}
