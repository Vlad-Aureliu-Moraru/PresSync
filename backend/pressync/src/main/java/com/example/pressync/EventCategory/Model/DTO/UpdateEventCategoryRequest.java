package com.example.pressync.EventCategory.Model.DTO;

import com.example.pressync.EventCategory.Model.RepeatableType;
import com.example.pressync.EventCategory.Model.RepeatsOnSpecificDay;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.sql.Time;
import java.time.LocalDate;

public record UpdateEventCategoryRequest(
        @NotBlank(message = "Class name cannot be empty")
        String name,

        @NotNull(message = "Starting time is required")
        Time startingTime,

        @NotNull(message = "End time is required")
        Time endTime,

        @NotNull(message = "Attendance window start time is required")
        Time attendanceTimeStart,

        @NotNull(message = "Duration is required")
        @Min(value = 5, message = "Duration must be at least 5 minutes")
        Integer attendanceDuration,

        Boolean repeatable,
        Integer configId,
        RepeatableType repeatableType,
        RepeatsOnSpecificDay repeatsOnSpecificDay,
        LocalDate baseDate
) {}
