package com.community.tool_library.services.impl;

import com.community.tool_library.dtos.NotificationDTO;
import com.community.tool_library.models.Notification;
import com.community.tool_library.models.User;
import com.community.tool_library.repositories.NotificationRepository;
import com.community.tool_library.services.NotificationService;
import com.community.tool_library.services.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserService userService;

    public NotificationServiceImpl(NotificationRepository notificationRepository, UserService userService) {
        this.notificationRepository = notificationRepository;
        this.userService = userService;
    }

    @Override
    public NotificationDTO createNotification(Long recipientId, String message) {
        User user = userService.getUserEntity(recipientId);
        Notification notification = Notification.builder()
                .recipient(user)
                .message(message)
                .read(false)
                .build();
        Notification saved = notificationRepository.save(notification);
        return mapToDTO(saved);
    }

    @Override
    public List<NotificationDTO> getUserNotifications(Long userId) {
        return notificationRepository.findAllByRecipientId(userId).stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found."));
        notification.setRead(true);
        notificationRepository.save(notification);
    }

    @Override
    public void markAllAsRead(Long userId) {
        List<Notification> notifications = notificationRepository.findAllByRecipientIdAndReadFalse(userId);
        notifications.forEach(notification -> notification.setRead(true));
        notificationRepository.saveAll(notifications);
    }

    @Override
    public long countUnreadNotifications(Long userId) {
        return notificationRepository.findAllByRecipientIdAndReadFalse(userId).size();
    }

    // helper method
    private NotificationDTO mapToDTO(Notification notification) {
        return new NotificationDTO(
                notification.getId(),
                notification.getRecipient().getId(),
                notification.getMessage(),
                notification.isRead(),
                notification.getCreatedAt()
        );
    }
}
