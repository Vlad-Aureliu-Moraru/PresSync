package com.example.pressync.Attendance.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceReportDTO {
    private String categoryName;
    private String createdByEmail;
    private AttendanceReportStats stats;
    private List<AttendanceReportRecord> records;
}
