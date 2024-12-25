package com.community.tool_library.services;

import com.community.tool_library.dtos.NotificationDTO;

import java.util.List;

public interface NotificationService {

        NotificationDTO createNotification(Long recipientId, String message);

        List<NotificationDTO> getUserNotifications(Long userId);

        void markAsRead(Long notificationId);

        void markAllAsRead(Long userId);

        long countUnreadNotifications(Long userId);

}
