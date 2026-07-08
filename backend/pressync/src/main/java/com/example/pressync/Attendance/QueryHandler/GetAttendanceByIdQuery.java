package com.example.pressync.Attendance.QueryHandler;

import com.example.pressync.Attendance.AttendanceRepository;
import com.example.pressync.Attendance.Model.AttendanceGetDTO;
import com.example.pressync.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class GetAttendanceByIdQuery implements Query<Integer, AttendanceGetDTO> {
    private final AttendanceRepository attendanceRepository;
    public GetAttendanceByIdQuery(AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
    }
    @Override
    public ResponseEntity<AttendanceGetDTO> execute(Integer input) {
        AttendanceGetDTO dto = attendanceRepository.findByIdWithDetails(input)
            .map(AttendanceGetDTO::new)
            .orElseThrow(() -> new IllegalArgumentException("Attendance with id " + input + " not found"));
        return ResponseEntity.ok(dto);
    }
}
