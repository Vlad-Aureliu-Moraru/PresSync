package com.example.pressync.Event.QueryHandlers;

import com.example.pressync.Event.EventRepository;
import com.example.pressync.Event.Model.Event;
import com.example.pressync.Event.Model.EventGetDTO;
import com.example.pressync.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetEventByIdQuery implements Query<Integer, EventGetDTO> {
    private final EventRepository eventRepository;
    @Override
    public ResponseEntity<EventGetDTO> execute(Integer input) {
        Event event = eventRepository.findById(input).orElse(null);
        if (event == null) {
            throw new IllegalArgumentException("Event not found");
        }
        return ResponseEntity.ok().body(new EventGetDTO(event));
    }


}
