package com.example.pressync.Attendance;

import com.example.pressync.Attendance.CommandHandler.CreateAttendanceCommand;
import com.example.pressync.Attendance.CommandHandler.UpdateAttendanceCommand;
import com.example.pressync.Attendance.Model.Attendance;
import com.example.pressync.Attendance.Model.AttendanceCreateDTO;
import com.example.pressync.Attendance.Model.AttendanceUpdateDTO;
import com.example.pressync.Attendance.QueryHandler.GetAllAttendanceQuery;
import com.example.pressync.Attendance.QueryHandler.GetAttendanceByIdQuery;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Attendance")
public class AttendanceController {
    private CreateAttendanceCommand createAttendanceCommand;
    private UpdateAttendanceCommand updateAttendanceCommand;
    private GetAllAttendanceQuery getAllAttendanceQuery;
    private GetAttendanceByIdQuery getAttendanceByIdQuery;
    public AttendanceController(GetAttendanceByIdQuery getAttendanceByIdQuery,CreateAttendanceCommand createAttendanceCommand, UpdateAttendanceCommand updateAttendanceCommand, GetAllAttendanceQuery getAllAttendanceQuery) {
        this.createAttendanceCommand = createAttendanceCommand;
        this.updateAttendanceCommand = updateAttendanceCommand;
        this.getAllAttendanceQuery = getAllAttendanceQuery;
        this.getAttendanceByIdQuery = getAttendanceByIdQuery;
    }
    @GetMapping
    public ResponseEntity<List<Attendance>> getAllAttendance() {
        return getAllAttendanceQuery.execute(null);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Attendance> getAttendanceById(@PathVariable  int id) {
        return getAttendanceByIdQuery.execute(id);
    }
    @PostMapping
    public ResponseEntity<Attendance> createAttendance(@RequestBody AttendanceCreateDTO attendance) {
        return createAttendanceCommand.execute(attendance);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateAttendanceById(@PathVariable int id, @RequestBody AttendanceCreateDTO attendance) {
        AttendanceUpdateDTO attendanceUpdateDTO = new AttendanceUpdateDTO(id, attendance);
        return updateAttendanceCommand.execute(attendanceUpdateDTO);
    }




}
