package com.example.pressync.Attendance.QueryHandler;

import com.example.pressync.Attendance.AttendanceRepository;
import com.example.pressync.Attendance.Model.*;
import com.example.pressync.EventCategory.EventCategoryRepository;
import com.example.pressync.EventCategory.Model.EventCategory;
import com.example.pressync.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GetAttendanceReportQuery implements Query<Integer, AttendanceReportDTO> {

    private final AttendanceRepository attendanceRepository;
    private final EventCategoryRepository eventCategoryRepository;

    public GetAttendanceReportQuery(AttendanceRepository attendanceRepository, EventCategoryRepository eventCategoryRepository) {
        this.attendanceRepository = attendanceRepository;
        this.eventCategoryRepository = eventCategoryRepository;
    }

    @Override
    public ResponseEntity<AttendanceReportDTO> execute(Integer categoryId) {
        throw new UnsupportedOperationException("Use execute with userId and isAdminOrMod instead");
    }

    public ResponseEntity<AttendanceReportDTO> execute(Integer categoryId, Integer userId, boolean isAdminOrMod) {
        EventCategory category = eventCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Event category not found"));

        List<Attendance> attendances;
        if (isAdminOrMod) {
            attendances = attendanceRepository.findAllByEventCategoryIdWithDetails(categoryId);
        } else {
            attendances = attendanceRepository.findAllByUserIdAndEventCategoryIdWithDetails(userId, categoryId);
        }

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        Map<Integer, Long> perEventCount = new LinkedHashMap<>();
        List<AttendanceReportRecord> records = new ArrayList<>();

        for (Attendance a : attendances) {
            int eventId = a.getEvent().getId();
            perEventCount.merge(eventId, 1L, Long::sum);

            records.add(new AttendanceReportRecord(
                    isAdminOrMod ? a.getUser().getName() : null,
                    isAdminOrMod ? a.getUser().getSurname() : null,
                    isAdminOrMod ? a.getUser().getEmail() : null,
                    a.getEvent().getDate() != null ? a.getEvent().getDate().format(dateFormatter) : "",
                    a.getJoinedAt() != null ? a.getJoinedAt().format(timeFormatter) : ""
            ));
        }

        long totalEvents = attendances.isEmpty() ? 0 :
                attendances.stream().map(a -> a.getEvent().getId()).distinct().count();

        long totalRecords = attendances.size();
        long peakAttendance = perEventCount.values().stream().mapToLong(Long::longValue).max().orElse(0);
        double averagePerEvent = totalEvents > 0 ? (double) totalRecords / totalEvents : 0;
        long overallRate;

        if (isAdminOrMod) {
            overallRate = peakAttendance > 0 ? Math.round(averagePerEvent / peakAttendance * 100) : 0;
        } else {
            overallRate = totalEvents > 0 ? Math.round((double) totalRecords / totalEvents * 100) : 0;
        }

        AttendanceReportStats stats = new AttendanceReportStats(
                totalEvents, totalRecords, averagePerEvent, peakAttendance, overallRate
        );

        String createdByEmail = category.getCreatedBy() != null ? category.getCreatedBy().getEmail() : "";

        AttendanceReportDTO dto = new AttendanceReportDTO(
                category.getName(), createdByEmail, stats, records
        );

        return ResponseEntity.ok(dto);
    }
}
