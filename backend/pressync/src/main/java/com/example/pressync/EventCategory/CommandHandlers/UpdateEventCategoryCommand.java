package com.example.pressync.EventCategory.CommandHandlers;

import com.example.pressync.Command;
import com.example.pressync.EventCategory.EventCategoryRepository;
import com.example.pressync.EventCategory.Model.EventCategory;
import com.example.pressync.EventCategory.Model.EventCategoryUpdateDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.http.HttpResponse;
@Service
public class UpdateEventCategoryCommand implements Command<EventCategoryUpdateDTO,String> {
    private EventCategoryRepository eventCategoryRepository;
    public UpdateEventCategoryCommand(EventCategoryRepository eventCategoryRepository) {
        this.eventCategoryRepository = eventCategoryRepository;
    }

    @Override
    public ResponseEntity<String> execute(EventCategoryUpdateDTO entity) {
        int id = entity.getId();
        EventCategory eventCategory = entity.getEventCategory();
        if(!eventCategoryRepository.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        eventCategory.setId(id);
        eventCategoryRepository.save(eventCategory);
        return ResponseEntity.ok().build();
    }
}
