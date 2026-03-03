package com.example.pressync.Attendance.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceUpdateDTO {
    private  int id;
    private AttendanceCreateDTO attendanceCreateDTO;
}
