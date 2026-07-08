package com.example.pressync.Attendance.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceReportStats {
    private long totalEvents;
    private long totalRecords;
    private double averagePerEvent;
    private long peakAttendance;
    private long overallRate;
}
