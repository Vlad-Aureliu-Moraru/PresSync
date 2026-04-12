package com.example.pressync.EventCategory.Model;

import com.example.pressync.Event.Model.Event;
import com.example.pressync.EventCategoryConfig.Model.EventCategoryConfig;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.sql.Time;
import java.time.LocalDate;
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
    private Boolean repeatable;

    @Column(name = "date")
    private LocalDate date;
    // --- THE NEW CONFIGURATION LINK ---
    // Many EventCategories can share ONE EventCategoryConfig
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "config_id") // This creates the 'config_id' foreign key in your database
    private EventCategoryConfig categoryConfig;

    // Keeps its original relationship to the actual Event instances
    @OneToMany(mappedBy = "eventCategory")
    @JsonIgnore
    private List<Event> events;
}