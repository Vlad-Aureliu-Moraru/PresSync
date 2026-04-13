package com.example.pressync.Attendance.CommandHandler;

import com.example.pressync.Attendance.AttendanceRepository;
import com.example.pressync.Attendance.Model.Attendance;
import com.example.pressync.Event.EventRepository;
import com.example.pressync.Event.Model.Event;
import com.example.pressync.EventCategory.Model.EventCategory;
import com.example.pressync.User.Model.User;
import com.example.pressync.User.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.sql.Time;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateAttendanceCommandTest {

    @Mock
    private AttendanceRepository attendanceRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private CreateAttendanceCommand createAttendanceCommand;

    private User user;
    private Event event;
    private EventCategory category;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1);

        category = new EventCategory();
        category.setName("Test Category");
        
        event = new Event();
        event.setId(1);
        event.setActive(true);
        event.setEventCategory(category);
    }

    @Test
    void testCreateAttendanceSuccess() {
        LocalTime now = LocalTime.now();
        category.setAttendanceTimeStart(Time.valueOf(now.minusMinutes(5)));
        category.setAttendanceDuration(15);

        when(eventRepository.findFirstByActiveTrue()).thenReturn(Optional.of(event));
        when(attendanceRepository.existsByUserIdAndEventId(anyInt(), anyInt())).thenReturn(false);

        ResponseEntity<String> response = createAttendanceCommand.execute(user);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Succesfully joined attendance", response.getBody());
        verify(attendanceRepository, times(1)).save(any(Attendance.class));
    }

    @Test
    void testCreateAttendanceNoActiveEvent() {
        when(eventRepository.findFirstByActiveTrue()).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> createAttendanceCommand.execute(user));

        assertEquals("There is no active event for now", exception.getMessage());
        verify(attendanceRepository, never()).save(any());
    }

    @Test
    void testCreateAttendanceAlreadyExists() {
        when(eventRepository.findFirstByActiveTrue()).thenReturn(Optional.of(event));
        when(attendanceRepository.existsByUserIdAndEventId(anyInt(), anyInt())).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> createAttendanceCommand.execute(user));

        assertEquals("Attendance already exists", exception.getMessage());
        verify(attendanceRepository, never()).save(any());
    }

    @Test
    void testCreateAttendanceTooEarly() {
        LocalTime now = LocalTime.now();
        category.setAttendanceTimeStart(Time.valueOf(now.plusMinutes(10)));
        category.setAttendanceDuration(15);

        when(eventRepository.findFirstByActiveTrue()).thenReturn(Optional.of(event));
        when(attendanceRepository.existsByUserIdAndEventId(anyInt(), anyInt())).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> createAttendanceCommand.execute(user));

        assertTrue(exception.getMessage().contains("Too early!"));
        verify(attendanceRepository, never()).save(any());
    }

    @Test
    void testCreateAttendanceTooLate() {
        LocalTime now = LocalTime.now();
        category.setAttendanceTimeStart(Time.valueOf(now.minusMinutes(20)));
        category.setAttendanceDuration(15); // End window at now - 5 mins

        when(eventRepository.findFirstByActiveTrue()).thenReturn(Optional.of(event));
        when(attendanceRepository.existsByUserIdAndEventId(anyInt(), anyInt())).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> createAttendanceCommand.execute(user));

        assertTrue(exception.getMessage().contains("Too late!"));
        verify(attendanceRepository, never()).save(any());
    }
}
