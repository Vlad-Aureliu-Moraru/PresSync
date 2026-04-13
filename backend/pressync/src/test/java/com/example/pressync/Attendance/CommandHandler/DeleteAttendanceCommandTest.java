package com.example.pressync.Attendance.CommandHandler;

import com.example.pressync.Attendance.AttendanceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteAttendanceCommandTest {

    @Mock
    private AttendanceRepository attendanceRepository;

    @InjectMocks
    private DeleteAttendanceCommand deleteAttendanceCommand;

    @Test
    void testDeleteAttendanceSuccess() {
        int id = 1;
        when(attendanceRepository.existsById(id)).thenReturn(true);

        ResponseEntity<String> response = deleteAttendanceCommand.execute(id);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Attendance with id 1 has been deleted", response.getBody());
        verify(attendanceRepository, times(1)).deleteById(id);
    }

    @Test
    void testDeleteAttendanceNotFound() {
        int id = 1;
        when(attendanceRepository.existsById(id)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> deleteAttendanceCommand.execute(id));

        assertEquals("Attendance with id 1 does not exist", exception.getMessage());
        verify(attendanceRepository, never()).deleteById(anyInt());
    }
}
