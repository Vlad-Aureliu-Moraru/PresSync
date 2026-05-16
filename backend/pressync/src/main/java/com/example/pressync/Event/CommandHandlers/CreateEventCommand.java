package com.example.pressync.Event.CommandHandlers;

import com.example.pressync.Command;
import com.example.pressync.Event.EventRepository;
import com.example.pressync.Event.Model.Event;
import com.example.pressync.EventCategory.EventCategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CreateEventCommand implements Command<Event,String> {
    private static final Logger log = LoggerFactory.getLogger(CreateEventCommand.class);
    private final EventRepository eventRepository;
    private final EventCategoryRepository eventCategoryRepository;
    public CreateEventCommand(EventRepository eventRepository, EventCategoryRepository eventCategoryRepository) {
        this.eventRepository = eventRepository;
        this.eventCategoryRepository = eventCategoryRepository;
    }
    @Override
    public ResponseEntity<String> execute(Event entity) {
        int eventCategoryId = entity.getEventCategory().getId();
        eventCategoryRepository.findById(eventCategoryId).orElseThrow(() -> new IllegalArgumentException("Event Category Not Found"));
        eventRepository.save(entity);
        return ResponseEntity.ok().body("Success");
    }
}
