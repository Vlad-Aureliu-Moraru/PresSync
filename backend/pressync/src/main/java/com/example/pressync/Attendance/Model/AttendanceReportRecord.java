package com.example.pressync.Attendance.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceReportRecord {
    private String name;
    private String surname;
    private String email;
    private String eventDate;
    private String joinedAt;
}
