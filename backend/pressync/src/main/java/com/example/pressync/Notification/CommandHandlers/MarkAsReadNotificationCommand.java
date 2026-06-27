package com.example.pressync.Notification.CommandHandlers;

import com.example.pressync.Command;
import com.example.pressync.Notification.Model.Notification;
import com.example.pressync.Notification.NotificationRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class MarkAsReadNotificationCommand implements Command<MarkAsReadNotificationCommand.MarkAsReadInput, String> {
    private final NotificationRepository notificationRepository;

    public MarkAsReadNotificationCommand(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public ResponseEntity<String> execute(MarkAsReadInput input) {
        Notification notification = notificationRepository.findById(input.notificationId())
                .orElseThrow(() -> new IllegalArgumentException("Notification not found"));

        if (!notification.getUser().getId().equals(input.userId())) {
            throw new IllegalArgumentException("Notification does not belong to this user");
        }

        notification.setRead(true);
        notificationRepository.save(notification);
        return ResponseEntity.ok().body("Marked as read");
    }

    public record MarkAsReadInput(Integer notificationId, Integer userId) {}
}
