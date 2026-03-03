package com.example.pressync.Attendance.CommandHandler;

import com.example.pressync.Attendance.AttendanceRepository;
import com.example.pressync.Command;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class DeleteAttendanceCommand implements Command<Integer,String> {
    private AttendanceRepository attendanceRepository;
    public DeleteAttendanceCommand(AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
    }
    @Override
    public ResponseEntity<String> execute(Integer entity) {
        if (!attendanceRepository.existsById(entity)) {
            throw new IllegalArgumentException("Attendance with id " + entity + " does not exist");
        }
        attendanceRepository.deleteById(entity);
        return ResponseEntity.ok("Attendance with id " + entity + " has been deleted");
    }
}
