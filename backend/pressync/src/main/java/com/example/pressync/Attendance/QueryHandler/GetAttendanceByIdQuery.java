package com.example.pressync.Attendance.QueryHandler;

import com.example.pressync.Attendance.AttendanceRepository;
import com.example.pressync.Attendance.Model.Attendance;
import com.example.pressync.Query;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class GetAttendanceByIdQuery implements Query<Integer, Attendance> {
    private AttendanceRepository attendanceRepository;
    public GetAttendanceByIdQuery(AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
    }
    @Override
    public ResponseEntity<Attendance> execute(Integer input) {
        Attendance found = attendanceRepository.findById(input).orElse(null);
        if (found == null) {
            throw new IllegalArgumentException("Attendance with id " + input + " not found");
        }
        return ResponseEntity.ok(found);
    }
}
