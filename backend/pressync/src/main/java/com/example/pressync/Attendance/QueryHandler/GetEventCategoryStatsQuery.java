package com.example.pressync.Attendance.QueryHandler;

import com.example.pressync.Attendance.AttendanceRepository;
import com.example.pressync.Attendance.Model.EventAttendanceSummary;
import com.example.pressync.Attendance.Model.EventCategoryStatsDTO;
import com.example.pressync.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetEventCategoryStatsQuery implements Query<Integer, EventCategoryStatsDTO> {

    private final AttendanceRepository attendanceRepository;

    public GetEventCategoryStatsQuery(AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
    }

    @Override
    public ResponseEntity<EventCategoryStatsDTO> execute(Integer categoryId) {
        List<Long> counts = attendanceRepository.countAttendancePerEventByCategory(categoryId);
        List<EventAttendanceSummary> summaries = attendanceRepository.getEventAttendanceSummariesByCategory(categoryId);

        if (counts == null || counts.isEmpty()) {
            return ResponseEntity.ok(new EventCategoryStatsDTO(0L, 0L, 0L, 0L, 0L, summaries));
        }

        long max = Long.MIN_VALUE;
        long min = Long.MAX_VALUE;
        long sum = 0;

        double weightedSum = 0;
        double weightTotal = 0;

        for (int i = 0; i < counts.size(); i++) {
            long count = counts.get(i);

            if (count > max) max = count;
            if (count < min) min = count;
            sum += count;

            int weight = i + 1;
            weightedSum += count * weight;
            weightTotal += weight;
        }

        long average = Math.round((double) sum / counts.size());
        long projected = Math.round(weightedSum / weightTotal);
        long attendanceRate = max > 0 ? Math.round((double) average / max * 100) : 0;

        EventCategoryStatsDTO stats = new EventCategoryStatsDTO(average, max, min, projected, attendanceRate, summaries);
        return ResponseEntity.ok(stats);
    }
}
