package com.example.pressync.EventCategory.Model.DTO;

import com.example.pressync.EventCategory.Model.EventCategory;
import com.example.pressync.EventCategoryConfig.Model.EventCategoryConfig;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.sql.Time;
import java.time.LocalDate;
@Data
public class EventCategoryGetDTO {
    private Integer id;
    private String name;
    private Time startingTime;
    private Time endTime;
    private Time attendanceTimeStart;
    private Integer attendanceDuration;
    private Boolean repeatable;
    private LocalDate specificDate;
    private EventCategoryConfig categoryConfig;

    public EventCategoryGetDTO(EventCategory eventCategory) {
        this.id = eventCategory.getId();
        this.name = eventCategory.getName();
        this.startingTime = eventCategory.getStartingTime();
        this.endTime = eventCategory.getEndTime();
        this.attendanceTimeStart = eventCategory.getAttendanceTimeStart();
        this.attendanceDuration = eventCategory.getAttendanceDuration();
        this.repeatable = eventCategory.getRepeatable();
        this.specificDate = eventCategory.getSpecificDate();
//        this.categoryConfig = eventCategory.getCategoryConfig();
    }
}
