package com.example.pressync.EventCategory.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;

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
    private String startingTime;
    @Column(name = "endTime")
    private String endTime;
    @Column(name = "attendanceTimeStart")
    private String attendanceTimeStart;
    @Column(name = "attendanceDuration")
    private String attendanceDuration;
    @Column(name = "repeatable")
    private  boolean repeatable;
    @Column(name = "repeatableType")
    private  String repeatableType;
    @Column(name = "repeatsAfterFinished")
    private  boolean repeatsAfterFinished;
    @Column(name = "repeatsOnSpecificDay")
    private  String repeatsOnSpecificDay;



}
