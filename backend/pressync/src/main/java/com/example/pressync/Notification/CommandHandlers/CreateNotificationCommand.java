package com.example.pressync.Notification.CommandHandlers;

import com.example.pressync.Command;
import com.example.pressync.Notification.Model.Notification;
import com.example.pressync.Notification.Model.NotificationCreateDTO;
import com.example.pressync.Notification.Model.NotificationGetDTO;
import com.example.pressync.Notification.NotificationRepository;
import com.example.pressync.User.Model.User;
import com.example.pressync.User.UserRepository;
import org.springframework.http.ResponseEntity;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CreateNotificationCommand implements Command<CreateNotificationCommand.CreateNotificationInput, NotificationGetDTO> {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public CreateNotificationCommand(NotificationRepository notificationRepository, UserRepository userRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public ResponseEntity<NotificationGetDTO> execute(CreateNotificationInput input) {
        User user = userRepository.findById(input.userId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Notification notification = new Notification();
        notification.setUser(user);
        notification.setType(input.dto().getType());
        notification.setTitle(input.dto().getTitle());
        notification.setMessage(input.dto().getMessage());
        notification.setRead(false);
        notification.setTimestamp(LocalDateTime.now());
        notification.setActionUrl(input.dto().getActionUrl());
        notification.setCategoryId(input.dto().getCategoryId());

        Notification saved = notificationRepository.save(notification);
        return ResponseEntity.ok(new NotificationGetDTO(saved));
    }

    public record CreateNotificationInput(Integer userId, NotificationCreateDTO dto) {}
}
