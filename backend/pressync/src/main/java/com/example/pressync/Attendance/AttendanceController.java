package com.example.pressync.Attendance;

import com.example.pressync.Attendance.CommandHandler.CreateAttendanceCommand;
import com.example.pressync.Attendance.CommandHandler.UpdateAttendanceCommand;
import com.example.pressync.Attendance.Model.*;
import com.example.pressync.Attendance.QueryHandler.*;
import com.example.pressync.User.Model.User;
import com.example.pressync.User.Model.UserRoles;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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
    private final GetAttendanceReportQuery getAttendanceReportQuery;
    private final ReportExportService reportExportService;

    @GetMapping("/stats/category/{categoryId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<EventCategoryStatsDTO> getEventCategoryStats(@PathVariable int categoryId) {
        return getEventCategoryStatsQuery.execute(categoryId);
    }

    @GetMapping("/user/{userId}/category/{categoryId}")
    @PreAuthorize("hasAnyRole('ADMIN','MODERATOR') or #userId == principal.id")
    public ResponseEntity<List<AttendanceGetDTO>> getAttendanceByUserIdAndCategoryId(@PathVariable int userId, @PathVariable int categoryId) {
        return getAttendanceByUserIdAndCategoryIdQuery.execute(new GetAttendanceByUserIdAndCategoryIdQuery.AttendanceByUserAndCategoryInput(userId, categoryId));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','MODERATOR')")
    public ResponseEntity<List<AttendanceGetDTO>> getAllAttendance() {
        return getAllAttendanceQuery.execute(null);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MODERATOR')")
    public ResponseEntity<AttendanceGetDTO> getAttendanceById(@PathVariable int id) {
        return getAttendanceByIdQuery.execute(id);
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN','MODERATOR') or #userId == principal.id")
    public ResponseEntity<List<AttendanceGetDTO>> getAttendanceByUserID(@PathVariable int userId) {
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

    @GetMapping("/report/category/{categoryId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<byte[]> exportAttendanceReport(
            @PathVariable int categoryId,
            @RequestParam String format,
            @AuthenticationPrincipal User currentUser) {
        if (!format.equals("csv") && !format.equals("pdf")) {
            throw new IllegalArgumentException("Invalid format. Use 'csv' or 'pdf'.");
        }

        boolean isAdminOrMod = currentUser.getRole() == UserRoles.ADMIN || currentUser.getRole() == UserRoles.MODERATOR;
        ResponseEntity<AttendanceReportDTO> response = getAttendanceReportQuery.execute(categoryId, currentUser.getId(), isAdminOrMod);
        AttendanceReportDTO report = response.getBody();
        if (report == null) {
            return ResponseEntity.noContent().build();
        }

        byte[] data;
        String extension;
        MediaType mediaType;

        if (format.equals("csv")) {
            data = reportExportService.generateCsv(report);
            extension = "csv";
            mediaType = MediaType.parseMediaType("text/csv");
        } else {
            data = reportExportService.generatePdf(report);
            extension = "pdf";
            mediaType = MediaType.APPLICATION_PDF;
        }

        String filename = "report-" + report.getCategoryName().replaceAll("\\s+", "_") + "." + extension;

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(mediaType)
                .body(data);
    }
}
