package com.example.pressync.EventCategory.Model;

import com.example.pressync.Event.Model.Event;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.util.List;

@Data
@Entity
@Table(name="CategorieEveniment")
@AllArgsConstructor
@NoArgsConstructor
public class EventCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer Id;
    @Column(name = "Name")
    private String Name;
    @Column(name = "StartingTime")
    private Time StartingTime;
    @Column(name = "EndTime")
    private Time EndTime;
    @Column(name = "AttendanceTimeStart")
    private Time AttendanceTimeStart;
    @Column(name = "AttendanceDuration")
    private Integer AttendanceDuration;
    @Column(name = "Repeatable")
    private  Boolean Repeatable;
    @Column(name = "RepeatableType")
    private  String RepeatableType;
    @Column(name = "RepeatsAfterFinished")
    private  Boolean RepeatsAfterFinished;
    @Column(name = "RepeatsOnSpecificDay")
    private  String repeatsOnSpecificDay;
    @OneToMany(mappedBy = "eventCategory")
    @JsonIgnore
    private List<Event> events;


}
