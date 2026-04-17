package com.example.pressync.Attendance.Model;

import com.example.pressync.Event.Model.EventGetDTO;
import com.example.pressync.Event.Model.EventPutDTO;
import com.example.pressync.User.Model.DTOSs.UserGetDTO;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AttendanceGetDTO {
    private Integer id;
    private UserGetDTO user;
    private EventGetDTO event;
    private LocalDateTime date;
    public AttendanceGetDTO(Attendance attendance) {
        this.id = attendance.getId();
        this.user = new UserGetDTO(attendance.getUser());
        this.event = new EventGetDTO(attendance.getEvent());
        this.date = attendance.getJoinedAt();

    }

}
