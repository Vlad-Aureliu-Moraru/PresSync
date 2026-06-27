package com.example.pressync.Notification.Model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationGetDTO {
    private Integer id;
    private String type;
    private String title;
    private String message;
    private Boolean read;
    private LocalDateTime timestamp;
    private String actionUrl;
    private Integer categoryId;

    public NotificationGetDTO(Notification notification) {
        this.id = notification.getId();
        this.type = notification.getType();
        this.title = notification.getTitle();
        this.message = notification.getMessage();
        this.read = notification.getRead();
        this.timestamp = notification.getTimestamp();
        this.actionUrl = notification.getActionUrl();
        this.categoryId = notification.getCategoryId();
    }
}
