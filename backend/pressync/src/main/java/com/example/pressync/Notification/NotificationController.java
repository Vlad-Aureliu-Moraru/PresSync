package com.example.pressync.Notification;

import com.example.pressync.Notification.CommandHandlers.CreateNotificationCommand;
import com.example.pressync.Notification.CommandHandlers.DeleteNotificationCommand;
import com.example.pressync.Notification.CommandHandlers.MarkAllAsReadNotificationCommand;
import com.example.pressync.Notification.CommandHandlers.MarkAsReadNotificationCommand;
import com.example.pressync.Notification.Model.Notification;
import com.example.pressync.Notification.Model.NotificationCreateDTO;
import com.example.pressync.Notification.Model.NotificationGetDTO;
import com.example.pressync.Notification.QueryHandlers.GetAllNotificationsQuery;
import com.example.pressync.Notification.QueryHandlers.GetUnreadNotificationsQuery;
import com.example.pressync.User.Model.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final GetAllNotificationsQuery getAllNotificationsQuery;
    private final GetUnreadNotificationsQuery getUnreadNotificationsQuery;
    private final CreateNotificationCommand createNotificationCommand;
    private final MarkAsReadNotificationCommand markAsReadNotificationCommand;
    private final MarkAllAsReadNotificationCommand markAllAsReadNotificationCommand;
    private final DeleteNotificationCommand deleteNotificationCommand;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<NotificationGetDTO>> getAll(@AuthenticationPrincipal User user) {
        return getAllNotificationsQuery.execute(user.getId());
    }

    @GetMapping("/unread")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<NotificationGetDTO>> getUnread(@AuthenticationPrincipal User user) {
        return getUnreadNotificationsQuery.execute(user.getId());
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<NotificationGetDTO> create(@AuthenticationPrincipal User user, @Valid @RequestBody NotificationCreateDTO dto) {
        return createNotificationCommand.execute(new CreateNotificationCommand.CreateNotificationInput(user.getId(), dto));
    }

    @PutMapping("/{id}/read")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> markAsRead(@PathVariable Integer id, @AuthenticationPrincipal User user) {
        return markAsReadNotificationCommand.execute(new MarkAsReadNotificationCommand.MarkAsReadInput(id, user.getId()));
    }

    @PutMapping("/read-all")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> markAllAsRead(@AuthenticationPrincipal User user) {
        return markAllAsReadNotificationCommand.execute(user.getId());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> dismiss(@PathVariable Integer id, @AuthenticationPrincipal User user) {
        return deleteNotificationCommand.execute(new DeleteNotificationCommand.DeleteNotificationInput(id, user.getId()));
    }
}
