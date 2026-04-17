package com.example.pressync.Attendance.QueryHandler;

import com.example.pressync.Attendance.AttendanceRepository;
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

        if (counts == null || counts.isEmpty()) {
            return ResponseEntity.ok(new EventCategoryStatsDTO(0L, 0L, 0L, 0L));
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

            int weight = i + 1; // more recent events (higher index due to ascending date order) get more weight
            weightedSum += count * weight;
            weightTotal += weight;
        }

        long average = sum / counts.size();
        long projected = Math.round(weightedSum / weightTotal);

        EventCategoryStatsDTO stats = new EventCategoryStatsDTO(average, max, min, projected);
        return ResponseEntity.ok(stats);
    }
}
