package com.example.pressync.Event.CommandHandlers;

import com.example.pressync.Command;
import com.example.pressync.Event.EventRepository;
import com.example.pressync.Event.Model.Event;
import com.example.pressync.Event.Model.EventPutDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UpdateEventCommand implements Command<EventPutDTO,String> {
    private EventRepository eventRepository;
    public UpdateEventCommand(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public ResponseEntity<String> execute(EventPutDTO entity) {
        int id = entity.getId();
        Event event = eventRepository.findById(id).orElse(null);
        if (event == null) {
            throw new IllegalArgumentException("Event with id " + id + " does not exist");
        }
        Event newEvent = entity.getEvent();
        newEvent.setId(id);
        eventRepository.save(newEvent);
        return ResponseEntity.ok().build();
    }
}
