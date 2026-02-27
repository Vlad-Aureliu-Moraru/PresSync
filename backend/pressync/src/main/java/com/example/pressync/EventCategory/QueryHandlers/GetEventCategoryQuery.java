package com.example.pressync.EventCategory.QueryHandlers;

import com.example.pressync.EventCategory.EventCategoryRepository;
import com.example.pressync.EventCategory.Model.EventCategory;
import com.example.pressync.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class GetEventCategoryQuery implements Query<Integer,EventCategory> {
    private EventCategoryRepository eventCategoryRepository;
    public GetEventCategoryQuery(EventCategoryRepository eventCategoryRepository) {
        this.eventCategoryRepository = eventCategoryRepository;
    }
    @Override
    public ResponseEntity<EventCategory> execute(Integer input) {
       EventCategory found =  eventCategoryRepository.findById(input).orElse(null);
       if(found == null){
            return ResponseEntity.notFound().build();
       }
       return ResponseEntity.ok().body(found);
    }
}
