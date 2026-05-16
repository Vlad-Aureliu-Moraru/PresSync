package com.example.pressync.Event.CommandHandlers;

import com.example.pressync.Command;
import com.example.pressync.Event.EventRepository;
import com.example.pressync.Event.Model.Event;
import com.example.pressync.Event.Model.EventPutDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UpdateEventCommand implements Command<EventPutDTO,String> {
    private final EventRepository eventRepository;

    public UpdateEventCommand(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public ResponseEntity<String> execute(EventPutDTO entity) {
        int id = entity.getId();
        Event existingEvent = eventRepository.findById(id).orElse(null);
        if (existingEvent == null) {
            throw new IllegalArgumentException("Event with id " + id + " does not exist");
        }
        Event incoming = entity.getEvent();
        if (incoming.getEventCategory() != null) {
            existingEvent.setEventCategory(incoming.getEventCategory());
        }
        if (incoming.getActive() != null) {
            existingEvent.setActive(incoming.getActive());
        }
        if (incoming.getArchived() != null) {
            existingEvent.setArchived(incoming.getArchived());
        }
        if (incoming.getDate() != null) {
            existingEvent.setDate(incoming.getDate());
        }
        eventRepository.save(existingEvent);
        return ResponseEntity.ok().build();
    }
}
