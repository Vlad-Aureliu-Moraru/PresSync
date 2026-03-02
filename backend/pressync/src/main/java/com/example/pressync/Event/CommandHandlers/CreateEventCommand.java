package com.example.pressync.Event.CommandHandlers;

import com.example.pressync.Command;
import com.example.pressync.Event.EventRepository;
import com.example.pressync.Event.Model.Event;
import com.example.pressync.EventCategory.EventCategoryRepository;
import com.example.pressync.EventCategory.Model.EventCategory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.http.HttpResponse;

@Service
public class CreateEventCommand implements Command<Event,String> {
    private EventRepository eventRepository;
    private EventCategoryRepository eventCategoryRepository;
    public CreateEventCommand(EventRepository eventRepository, EventCategoryRepository eventCategoryRepository) {
        this.eventRepository = eventRepository;
        this.eventCategoryRepository = eventCategoryRepository;
    }
    @Override
    public ResponseEntity<String> execute(Event entity) {
        int eventCategoryId= entity.getEventCategory().getId();
        eventCategoryRepository.findById(eventCategoryId).orElseThrow(()->new IllegalArgumentException("Event Category Not Found"));

        try {
            eventRepository.save(entity);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().body("Success");
    }
}
