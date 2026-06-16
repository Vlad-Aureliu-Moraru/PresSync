package com.example.pressync.Attendance.Model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceCreateDTO {
    @NotNull(message = "User ID is required")
    @Min(value = 1, message = "User ID must be positive")
    private int userId;

    @NotNull(message = "Event ID is required")
    @Min(value = 1, message = "Event ID must be positive")
    private int eventId;
}
