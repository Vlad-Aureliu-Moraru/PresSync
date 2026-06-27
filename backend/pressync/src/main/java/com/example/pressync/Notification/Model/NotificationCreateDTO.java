package com.example.pressync.Notification.Model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationCreateDTO {
    @NotBlank(message = "Type is required")
    private String type;

    @NotBlank(message = "Title is required")
    private String title;

    private String message;

    private String actionUrl;

    private Integer categoryId;
}
