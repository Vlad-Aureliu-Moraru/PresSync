package com.example.pressync.Event.Model;

import com.example.pressync.EventCategory.Model.EventCategory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "Event")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer Id;
    @ManyToOne
    private EventCategory eventCategory;
    @Column(name = "Active")
    private Boolean Active;
    @Column(name = "Archived")
    private Boolean Archived;

}
