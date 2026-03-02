package com.example.pressync.EventCategory.CommandHandlers;

import com.example.pressync.Command;
import com.example.pressync.EventCategory.EventCategoryRepository;
import com.example.pressync.EventCategory.Model.EventCategory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.http.HttpResponse;
@Service
public class DeleteEventCategoryCommand implements Command<Integer, HttpResponse> {
    private EventCategoryRepository eventCategoryRepository;
    public DeleteEventCategoryCommand(EventCategoryRepository eventCategoryRepository) {
        this.eventCategoryRepository = eventCategoryRepository;
    }
    @Override
    public ResponseEntity<HttpResponse> execute(Integer entity) {
        EventCategory found =  eventCategoryRepository.findById(entity).orElse(null);
        if(found == null){
            throw new IllegalArgumentException("Event category with id " + entity + " does not exist.");
        }
        eventCategoryRepository.delete(found);
        return ResponseEntity.ok().build();
    }
}
