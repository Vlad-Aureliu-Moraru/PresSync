package com.example.pressync.Event.QueryHandlers;

import com.example.pressync.Event.EventRepository;
import com.example.pressync.Event.Model.Event;
import com.example.pressync.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetAllEventsQuery implements Query<Void,List<Event>> {
    private EventRepository eventRepository;
    public GetAllEventsQuery(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }


    @Override
    public ResponseEntity<List<Event>> execute(Void input) {
        return ResponseEntity.ok().body(eventRepository.findAll());
    }
}
