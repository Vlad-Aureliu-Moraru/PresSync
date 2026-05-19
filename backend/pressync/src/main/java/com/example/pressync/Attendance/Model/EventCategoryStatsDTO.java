package com.example.pressync.Attendance.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventCategoryStatsDTO {
    private Long averageAttendance;
    private Long maxAttendance;
    private Long minAttendance;
    private Long projectedNextAttendance;
    private Long attendanceRate;
    private List<EventAttendanceSummary> eventAttendanceSummaries;
}
