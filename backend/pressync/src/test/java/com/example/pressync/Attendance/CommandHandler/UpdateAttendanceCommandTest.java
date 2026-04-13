package com.example.pressync.Attendance.CommandHandler;

import com.example.pressync.Attendance.AttendanceRepository;
import com.example.pressync.Attendance.Model.Attendance;
import com.example.pressync.Attendance.Model.AttendanceCreateDTO;
import com.example.pressync.Attendance.Model.AttendanceUpdateDTO;
import com.example.pressync.Event.EventRepository;
import com.example.pressync.Event.Model.Event;
import com.example.pressync.User.Model.User;
import com.example.pressync.User.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateAttendanceCommandTest {

    @Mock
    private AttendanceRepository attendanceRepository;
    @Mock
    private EventRepository eventRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UpdateAttendanceCommand updateAttendanceCommand;

    private AttendanceUpdateDTO updateDTO;
    private AttendanceCreateDTO createDTO;
    private User user;
    private Event event;

    @BeforeEach
    void setUp() {
        createDTO = new AttendanceCreateDTO();
        createDTO.setUserId(1);
        createDTO.setEventId(1);

        updateDTO = new AttendanceUpdateDTO();
        updateDTO.setId(1);
        updateDTO.setAttendanceCreateDTO(createDTO);

        user = new User();
        user.setId(1);

        event = new Event();
        event.setId(1);
        event.setActive(true);
    }

    @Test
    void testUpdateAttendanceSuccess() {
        when(attendanceRepository.existsById(1)).thenReturn(true);
        when(userRepository.existsById(1)).thenReturn(true);
        when(eventRepository.existsById(1)).thenReturn(true);
        when(eventRepository.findById(1)).thenReturn(Optional.of(event));
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        ResponseEntity<String> response = updateAttendanceCommand.execute(updateDTO);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Successfully updated attendance", response.getBody());
        verify(attendanceRepository, times(1)).save(any(Attendance.class));
    }

    @Test
    void testUpdateAttendanceNotFound() {
        when(attendanceRepository.existsById(1)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> updateAttendanceCommand.execute(updateDTO));

        assertTrue(exception.getMessage().contains("Attendance with id 1 does not exist"));
        verify(attendanceRepository, never()).save(any());
    }

    @Test
    void testUpdateAttendanceUserNotFound() {
        when(attendanceRepository.existsById(1)).thenReturn(true);
        when(userRepository.existsById(1)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> updateAttendanceCommand.execute(updateDTO));

        assertEquals("User does not exist", exception.getMessage());
        verify(attendanceRepository, never()).save(any());
    }

    @Test
    void testUpdateAttendanceEventNotFound() {
        when(attendanceRepository.existsById(1)).thenReturn(true);
        when(userRepository.existsById(1)).thenReturn(true);
        when(eventRepository.existsById(1)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> updateAttendanceCommand.execute(updateDTO));

        assertEquals("Event does not exist", exception.getMessage());
        verify(attendanceRepository, never()).save(any());
    }

    @Test
    void testUpdateAttendanceEventNotActive() {
        event.setActive(false);
        when(attendanceRepository.existsById(1)).thenReturn(true);
        when(userRepository.existsById(1)).thenReturn(true);
        when(eventRepository.existsById(1)).thenReturn(true);
        when(eventRepository.findById(1)).thenReturn(Optional.of(event));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> updateAttendanceCommand.execute(updateDTO));

        assertEquals("Event is not active", exception.getMessage());
        verify(attendanceRepository, never()).save(any());
    }
}
