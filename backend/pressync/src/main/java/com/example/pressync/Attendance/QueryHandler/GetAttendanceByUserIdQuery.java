package com.example.pressync.Attendance.QueryHandler;

import com.example.pressync.Attendance.AttendanceRepository;
import com.example.pressync.Attendance.Model.Attendance;
import com.example.pressync.Query;
import com.example.pressync.User.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAttendanceByUserIdQuery implements Query<Integer, List<Attendance>> {
    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;


    @Override
    public ResponseEntity<List<Attendance>> execute(Integer input) {
        if (!userRepository.findById(input).isPresent()) {
            throw new IllegalArgumentException("User does not exist");
        }
        List<Attendance> list= attendanceRepository.findAllByUserId(input);
        return ResponseEntity.ok().body(list);
    }
}
