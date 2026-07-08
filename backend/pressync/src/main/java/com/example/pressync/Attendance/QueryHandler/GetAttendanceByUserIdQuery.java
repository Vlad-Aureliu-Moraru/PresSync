package com.example.pressync.Attendance.QueryHandler;

import com.example.pressync.Attendance.AttendanceRepository;
import com.example.pressync.Attendance.Model.AttendanceGetDTO;
import com.example.pressync.Query;
import com.example.pressync.User.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAttendanceByUserIdQuery implements Query<Integer, List<AttendanceGetDTO>> {
    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;


    @Override
    public ResponseEntity<List<AttendanceGetDTO>> execute(Integer input) {
        if (!userRepository.existsById(input)) {
            throw new IllegalArgumentException("User does not exist");
        }
        List<AttendanceGetDTO> list = attendanceRepository.findAllByUserIdWithDetails(input)
                .stream().map(AttendanceGetDTO::new).toList();
        return ResponseEntity.ok().body(list);
    }
}
