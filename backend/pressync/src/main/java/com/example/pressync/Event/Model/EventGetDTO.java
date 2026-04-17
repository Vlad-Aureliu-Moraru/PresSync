package com.example.pressync.Event.Model;

import com.example.pressync.EventCategory.Model.DTO.EventCategoryGetDTO;
import com.example.pressync.EventCategory.Model.EventCategory;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
@Data
public class EventGetDTO {
    private Integer id;
    private EventCategoryGetDTO eventCategory;
    private Boolean active;
    private Boolean archived;
    private LocalDate date;
    public EventGetDTO(Event e) {
        this.id = e.getId();
        this.eventCategory = new EventCategoryGetDTO(e.getEventCategory());
        this.active = e.getActive();
        this.archived = e.getArchived();
        this.date = e.getDate();
    }
}
