package com.example.pressync.Event.QueryHandlers;

import com.example.pressync.Event.EventRepository;
import com.example.pressync.Event.Model.Event;
import com.example.pressync.Event.Model.EventGetDTO;
import com.example.pressync.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAllEventsQuery implements Query<Void,List<EventGetDTO>> {
    private final EventRepository eventRepository;

    @Override
    public ResponseEntity<List<EventGetDTO>> execute(Void input) {
        return ResponseEntity.ok().body(eventRepository.findAllWithCategoriesAndConfigs().stream().map(EventGetDTO::new).toList());
    }
}
