package com.example.pressync.Attendance;

import com.example.pressync.Attendance.CommandHandler.CreateAttendanceCommand;
import com.example.pressync.Attendance.CommandHandler.UpdateAttendanceCommand;
import com.example.pressync.Attendance.Model.*;
import com.example.pressync.Attendance.QueryHandler.GetAllAttendanceQuery;
import com.example.pressync.Attendance.QueryHandler.GetAttendanceByIdQuery;
import com.example.pressync.Attendance.QueryHandler.GetAttendanceByUserIdQuery;
import com.example.pressync.Attendance.QueryHandler.GetEventCategoryStatsQuery;
import com.example.pressync.User.Model.User;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    private final GetEventCategoryStatsQuery getEventCategoryStatsQuery;

    @GetMapping("/stats/category/{categoryId}")
    public ResponseEntity<EventCategoryStatsDTO> getEventCategoryStats(@PathVariable int categoryId) {
        return getEventCategoryStatsQuery.execute(categoryId);
    }

    @GetMapping
    public ResponseEntity<List<AttendanceGetDTO>> getAllAttendance() {
        return getAllAttendanceQuery.execute(null);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Attendance> getAttendanceById(@PathVariable  int id) {
        return getAttendanceByIdQuery.execute(id);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Attendance>> getAttendanceByUserID (@PathVariable int userId) {
        return getAttendanceByUserIdQuery.execute(userId);
    }
    @PostMapping("/mark")
    public ResponseEntity<String> createAttendance(@AuthenticationPrincipal User user) {
        return createAttendanceCommand.execute(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateAttendanceById(@PathVariable int id, @RequestBody AttendanceCreateDTO attendance) {
        AttendanceUpdateDTO attendanceUpdateDTO = new AttendanceUpdateDTO(id, attendance);
        return updateAttendanceCommand.execute(attendanceUpdateDTO);
    }




}
