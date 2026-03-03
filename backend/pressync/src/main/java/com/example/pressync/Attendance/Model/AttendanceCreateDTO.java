package com.example.pressync.Attendance.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceCreateDTO {
    int userId;
    int eventId;
}
