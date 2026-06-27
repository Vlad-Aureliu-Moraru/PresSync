package com.example.pressync.Notification.QueryHandlers;

import com.example.pressync.Notification.Model.NotificationGetDTO;
import com.example.pressync.Notification.NotificationRepository;
import com.example.pressync.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAllNotificationsQuery implements Query<Integer, List<NotificationGetDTO>> {
    private final NotificationRepository notificationRepository;

    @Override
    public ResponseEntity<List<NotificationGetDTO>> execute(Integer userId) {
        return ResponseEntity.ok(
                notificationRepository.findAllByUserIdOrderByTimestampDesc(userId)
                        .stream()
                        .map(NotificationGetDTO::new)
                        .toList()
        );
    }
}
