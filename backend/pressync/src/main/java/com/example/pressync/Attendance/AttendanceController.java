package com.example.pressync.Attendance;

import com.example.pressync.Attendance.CommandHandler.CreateAttendanceCommand;
import com.example.pressync.Attendance.CommandHandler.UpdateAttendanceCommand;
import com.example.pressync.Attendance.Model.*;
import com.example.pressync.Attendance.QueryHandler.GetAllAttendanceQuery;
import com.example.pressync.Attendance.QueryHandler.GetAttendanceByIdQuery;
import com.example.pressync.Attendance.QueryHandler.GetAttendanceByUserIdAndCategoryIdQuery;
import com.example.pressync.Attendance.QueryHandler.GetAttendanceByUserIdQuery;
import com.example.pressync.Attendance.QueryHandler.GetEventCategoryStatsQuery;
import com.example.pressync.User.Model.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private final GetAttendanceByUserIdAndCategoryIdQuery getAttendanceByUserIdAndCategoryIdQuery;

    @GetMapping("/stats/category/{categoryId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<EventCategoryStatsDTO> getEventCategoryStats(@PathVariable int categoryId) {
        return getEventCategoryStatsQuery.execute(categoryId);
    }

    @GetMapping("/user/{userId}/category/{categoryId}")
    @PreAuthorize("hasAnyRole('ADMIN','MODERATOR') or #userId == principal.id")
    public ResponseEntity<List<Attendance>> getAttendanceByUserIdAndCategoryId(@PathVariable int userId, @PathVariable int categoryId) {
        return getAttendanceByUserIdAndCategoryIdQuery.execute(new GetAttendanceByUserIdAndCategoryIdQuery.AttendanceByUserAndCategoryInput(userId, categoryId));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','MODERATOR')")
    public ResponseEntity<List<AttendanceGetDTO>> getAllAttendance() {
        return getAllAttendanceQuery.execute(null);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MODERATOR')")
    public ResponseEntity<Attendance> getAttendanceById(@PathVariable int id) {
        return getAttendanceByIdQuery.execute(id);
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN','MODERATOR') or #userId == principal.id")
    public ResponseEntity<List<Attendance>> getAttendanceByUserID(@PathVariable int userId) {
        return getAttendanceByUserIdQuery.execute(userId);
    }

    @PostMapping("/mark")
    @PreAuthorize("hasAnyRole('USER','MODERATOR','ADMIN')")
    public ResponseEntity<String> createAttendance(@AuthenticationPrincipal User user) {
        return createAttendanceCommand.execute(user);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MODERATOR')")
    public ResponseEntity<String> updateAttendanceById(@PathVariable int id, @Valid @RequestBody AttendanceCreateDTO attendance) {
        AttendanceUpdateDTO attendanceUpdateDTO = new AttendanceUpdateDTO(id, attendance);
        return updateAttendanceCommand.execute(attendanceUpdateDTO);
    }
}
