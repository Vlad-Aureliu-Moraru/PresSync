package com.example.pressync.EventCategory.CommandHandlers;

import com.example.pressync.Command;
import com.example.pressync.EventCategory.EventCategoryRepository;
import com.example.pressync.EventCategory.Model.EventCategory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CreateEventCategoryCommand implements Command<EventCategory,String> {
    private EventCategoryRepository eventCategoryRepository;
    public CreateEventCategoryCommand(EventCategoryRepository eventCategoryRepository){
        this.eventCategoryRepository = eventCategoryRepository;
    }
    @Override
    public ResponseEntity<String> execute(EventCategory entity) {
        eventCategoryRepository.save(entity);
        return ResponseEntity.ok().build();
    }
}
