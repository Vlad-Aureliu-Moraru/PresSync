package com.example.pressync.Attendance;

import com.example.pressync.Attendance.CommandHandler.CreateAttendanceCommand;
import com.example.pressync.Attendance.CommandHandler.UpdateAttendanceCommand;
import com.example.pressync.Attendance.Model.Attendance;
import com.example.pressync.Attendance.Model.AttendanceCreateDTO;
import com.example.pressync.Attendance.Model.AttendanceUpdateDTO;
import com.example.pressync.Attendance.QueryHandler.GetAllAttendanceQuery;
import com.example.pressync.Attendance.QueryHandler.GetAttendanceByIdQuery;
import com.example.pressync.Attendance.QueryHandler.GetAttendanceByUserIdQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/attendance")
@RequiredArgsConstructor
public class AttendanceController {
    private final CreateAttendanceCommand createAttendanceCommand;
    private final UpdateAttendanceCommand updateAttendanceCommand;
    private final GetAllAttendanceQuery getAllAttendanceQuery;
    private final GetAttendanceByIdQuery getAttendanceByIdQuery;
    private final GetAttendanceByUserIdQuery getAttendanceByUserIdQuery;

    @GetMapping
    public ResponseEntity<List<Attendance>> getAllAttendance() {
        return getAllAttendanceQuery.execute(null);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Attendance> getAttendanceById(@PathVariable  int id) {
        return getAttendanceByIdQuery.execute(id);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Attendance>> getAttendanceByUserID (@PathVariable  int userId) {
        return getAttendanceByUserIdQuery.execute(userId);
    }
    @PostMapping("/{id}")
    public ResponseEntity<Attendance> createAttendance(@PathVariable int id) {
        return createAttendanceCommand.execute(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateAttendanceById(@PathVariable int id, @RequestBody AttendanceCreateDTO attendance) {
        AttendanceUpdateDTO attendanceUpdateDTO = new AttendanceUpdateDTO(id, attendance);
        return updateAttendanceCommand.execute(attendanceUpdateDTO);
    }




}
