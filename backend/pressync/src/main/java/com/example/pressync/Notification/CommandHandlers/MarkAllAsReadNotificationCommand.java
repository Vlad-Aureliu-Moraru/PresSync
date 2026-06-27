package com.example.pressync.Notification.CommandHandlers;

import com.example.pressync.Command;
import com.example.pressync.Notification.NotificationRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class MarkAllAsReadNotificationCommand implements Command<Integer, String> {
    private final NotificationRepository notificationRepository;

    public MarkAllAsReadNotificationCommand(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public ResponseEntity<String> execute(Integer userId) {
        notificationRepository.markAllAsReadByUserId(userId);
        return ResponseEntity.ok().body("All marked as read");
    }
}
