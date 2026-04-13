package com.example.pressync.Attendance.QueryHandler;

import com.example.pressync.Attendance.AttendanceRepository;
import com.example.pressync.Attendance.Model.EventCategoryStatsDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetEventCategoryStatsQueryTest {

    @Mock
    private AttendanceRepository attendanceRepository;

    @InjectMocks
    private GetEventCategoryStatsQuery getEventCategoryStatsQuery;

    @Test
    void testGetStatsEmptyData() {
        int categoryId = 1;
        when(attendanceRepository.countAttendancePerEventByCategory(categoryId)).thenReturn(Collections.emptyList());

        ResponseEntity<EventCategoryStatsDTO> response = getEventCategoryStatsQuery.execute(categoryId);

        assertEquals(200, response.getStatusCode().value());
        EventCategoryStatsDTO stats = response.getBody();
        assertNotNull(stats);
        assertEquals(0L, stats.getAverageAttendance());
        assertEquals(0L, stats.getMaxAttendance());
        assertEquals(0L, stats.getMinAttendance());
        assertEquals(0L, stats.getProjectedNextAttendance());
    }

    @Test
    void testGetStatsWithData() {
        int categoryId = 1;
        List<Long> counts = Arrays.asList(10L, 20L, 30L);
        when(attendanceRepository.countAttendancePerEventByCategory(categoryId)).thenReturn(counts);

        ResponseEntity<EventCategoryStatsDTO> response = getEventCategoryStatsQuery.execute(categoryId);

        assertEquals(200, response.getStatusCode().value());
        EventCategoryStatsDTO stats = response.getBody();
        assertNotNull(stats);
        
        // Average: (10+20+30)/3 = 20
        assertEquals(20L, stats.getAverageAttendance());
        // Max: 30
        assertEquals(30L, stats.getMaxAttendance());
        // Min: 10
        assertEquals(10L, stats.getMinAttendance());
        // Projected: (10*1 + 20*2 + 30*3) / (1+2+3) = 140 / 6 = 23.33... -> Round to 23
        assertEquals(23L, stats.getProjectedNextAttendance());
    }

    @Test
    void testGetStatsNullData() {
        int categoryId = 1;
        when(attendanceRepository.countAttendancePerEventByCategory(categoryId)).thenReturn(null);

        ResponseEntity<EventCategoryStatsDTO> response = getEventCategoryStatsQuery.execute(categoryId);

        assertEquals(200, response.getStatusCode().value());
        EventCategoryStatsDTO stats = response.getBody();
        assertNotNull(stats);
        assertEquals(0L, stats.getAverageAttendance());
    }
}
