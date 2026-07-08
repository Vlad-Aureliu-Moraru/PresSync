package com.example.pressync.Event.Model;

import com.example.pressync.EventCategory.Model.EventCategory;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Table(name = "Evenimente")
@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @ManyToOne
    private EventCategory eventCategory;
    @Column(name = "active")
    private Boolean active;
    @Column(name = "archived")
    private Boolean archived;
    @Column(name = "date")
    private LocalDate date;

}
