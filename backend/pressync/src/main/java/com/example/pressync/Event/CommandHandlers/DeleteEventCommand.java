package com.example.pressync.Event.CommandHandlers;

import com.example.pressync.Command;
import com.example.pressync.Event.EventRepository;
import com.example.pressync.Event.Model.Event;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class DeleteEventCommand implements Command<Integer,String> {
    private EventRepository eventRepository;
    public DeleteEventCommand(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }
    @Override
    public ResponseEntity<String> execute(Integer id) {
        Event event = eventRepository.findById(id).orElse(null);
        if (event == null) {
            throw new IllegalArgumentException("Event with id " + id + " does not exist");
        }
        eventRepository.delete(event);
        return ResponseEntity.ok().build();
    }
}
