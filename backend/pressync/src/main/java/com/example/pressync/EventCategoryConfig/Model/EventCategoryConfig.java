package com.example.pressync.EventCategoryConfig.Model;

import com.example.pressync.EventCategory.Model.EventCategory;
import com.example.pressync.EventCategory.Model.RepeatableType;
import com.example.pressync.EventCategory.Model.RepeatsOnSpecificDay;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name="EventCategoryConfig")
@AllArgsConstructor
@NoArgsConstructor
public class EventCategoryConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "repeatableType")
    private RepeatableType repeatableType;

    @Enumerated(EnumType.STRING)
    @Column(name = "repeatsOnSpecificDay")
    private RepeatsOnSpecificDay repeatsOnSpecificDay;

    @Column(name ="base_date")
    private LocalDate baseDate;

    @OneToMany(mappedBy = "categoryConfig", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<EventCategory> categories;
}