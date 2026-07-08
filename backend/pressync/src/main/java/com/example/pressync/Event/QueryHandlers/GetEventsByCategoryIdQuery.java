package com.example.pressync.Event.QueryHandlers;

import com.example.pressync.Event.EventRepository;
import com.example.pressync.Event.Model.EventGetDTO;
import com.example.pressync.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetEventsByCategoryIdQuery implements Query<Integer, List<EventGetDTO>> {
    private final EventRepository eventRepository;

    @Override
    public ResponseEntity<List<EventGetDTO>> execute(Integer input) {
        List<EventGetDTO> events = eventRepository.findAllByEventCategoryIdWithCategory(input)
                .stream()
                .map(EventGetDTO::new)
                .toList();
        return ResponseEntity.ok().body(events);
    }
}
