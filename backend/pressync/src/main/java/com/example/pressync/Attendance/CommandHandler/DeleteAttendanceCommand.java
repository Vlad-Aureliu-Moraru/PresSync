package com.example.pressync.Attendance.CommandHandler;

import com.example.pressync.Attendance.AttendanceRepository;
import com.example.pressync.Command;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteAttendanceCommand implements Command<Integer,String> {
    private final AttendanceRepository attendanceRepository;
    @Override
    public ResponseEntity<String> execute(Integer entity) {
        if (!attendanceRepository.existsById(entity)) {
            throw new IllegalArgumentException("Attendance with id " + entity + " does not exist");
        }
        attendanceRepository.deleteById(entity);
        return ResponseEntity.ok("Attendance with id " + entity + " has been deleted");
    }
}
