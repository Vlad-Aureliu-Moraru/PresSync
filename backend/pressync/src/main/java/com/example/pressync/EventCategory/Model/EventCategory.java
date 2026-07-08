package com.example.pressync.EventCategory.Model;

import com.example.pressync.Event.Model.Event;
import com.example.pressync.EventCategoryConfig.Model.EventCategoryConfig;
import com.example.pressync.User.Model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.sql.Time;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Entity
@Table(name="CategoriiEvenimente")
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
    private LocalDate specificDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    @JsonIgnoreProperties({"password", "authorities", "accountNonExpired", "accountNonLocked", "credentialsNonExpired", "enabled", "username", "mfaCode", "mfaExpiry", "failedLoginAttempts", "accountLockedUntil", "active", "mfaEnabled"})
    @ToString.Exclude
    private User createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "config_id")
    @ToString.Exclude
    private EventCategoryConfig categoryConfig;

    @OneToMany(mappedBy = "eventCategory")
    @JsonIgnore
    @ToString.Exclude
    private List<Event> events;

}