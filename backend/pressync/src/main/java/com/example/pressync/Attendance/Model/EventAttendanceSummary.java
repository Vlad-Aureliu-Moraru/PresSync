package com.example.pressync.Attendance.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventAttendanceSummary {
    private LocalDate date;
    private Long count;
}
