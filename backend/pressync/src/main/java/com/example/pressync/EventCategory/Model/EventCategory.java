package com.example.pressync.EventCategory.Model;

import com.example.pressync.Event.Model.Event;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

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
    @Column(name = "id")
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "startingTime")
    private Time startingTime;
    @Column(name = "endTime")
    private Time endTime;
    @Column(name = "attendanceTimeStart")
    private Time attendanceTimeStart;
    @Column(name = "attendanceDuration")
    @NotNull(message = "Duration is required")
    private Integer attendanceDuration;
    @Column(name = "repeatable")
    private  Boolean repeatable;
    @Column(name = "repeatableType")
    private  String repeatableType;
    @Column(name = "repeatsAfterFinished")
    private  Boolean repeatsAfterFinished;
    @Column(name = "repeatsOnSpecificDay")
    private  String repeatsOnSpecificDay;
    @OneToMany(mappedBy = "eventCategory")
    @JsonIgnore
    private List<Event> events;


}
