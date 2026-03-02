package com.example.pressync.Event.QueryHandlers;

import com.example.pressync.Event.EventRepository;
import com.example.pressync.Event.Model.Event;
import com.example.pressync.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class GetEventByIdQuery implements Query<Integer,Event> {
    private EventRepository eventRepository;
    public GetEventByIdQuery(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }
    @Override
    public ResponseEntity<Event> execute(Integer input) {
        Event event = eventRepository.findById(input).orElse(null);
        if (event == null) {
            throw new IllegalArgumentException("Event not found");
        }
        return ResponseEntity.ok().body(event);
    }


}
