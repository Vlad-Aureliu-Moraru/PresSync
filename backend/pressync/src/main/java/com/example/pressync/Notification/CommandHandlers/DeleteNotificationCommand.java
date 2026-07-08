package com.example.pressync.Notification.CommandHandlers;

import com.example.pressync.Command;
import com.example.pressync.Notification.Model.Notification;
import com.example.pressync.Notification.NotificationRepository;
import org.springframework.http.ResponseEntity;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class DeleteNotificationCommand implements Command<DeleteNotificationCommand.DeleteNotificationInput, String> {
    private final NotificationRepository notificationRepository;

    public DeleteNotificationCommand(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    @Transactional
    public ResponseEntity<String> execute(DeleteNotificationInput input) {
        Notification notification = notificationRepository.findById(input.notificationId())
                .orElseThrow(() -> new IllegalArgumentException("Notification not found"));

        if (!notification.getUser().getId().equals(input.userId())) {
            throw new IllegalArgumentException("Notification does not belong to this user");
        }

        notificationRepository.delete(notification);
        return ResponseEntity.ok().body("Deleted");
    }

    public record DeleteNotificationInput(Integer notificationId, Integer userId) {}
}
