package com.example.pressync.Attendance.QueryHandler;

import com.example.pressync.Attendance.AttendanceRepository;
import com.example.pressync.Attendance.Model.Attendance;
import com.example.pressync.Attendance.Model.AttendanceGetDTO;
import com.example.pressync.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetAllAttendanceQuery implements Query<Void, List<AttendanceGetDTO>> {
    private AttendanceRepository attendanceRepository;
    public GetAllAttendanceQuery(AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
    }
    @Override
    public ResponseEntity<List<AttendanceGetDTO>> execute(Void input) {
        return ResponseEntity.ok().body(attendanceRepository.findAll().stream().map(AttendanceGetDTO::new).toList());
    }
}
