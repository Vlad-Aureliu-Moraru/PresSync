package com.example.pressync.EventCategory.QueryHandlers;

import com.example.pressync.EventCategory.EventCategoryRepository;
import com.example.pressync.EventCategory.Model.EventCategory;
import com.example.pressync.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class GetAllEventCategoriesQuerry implements Query<Void, List<EventCategory>> {
    private EventCategoryRepository eventCategoryRepository;
    public GetAllEventCategoriesQuerry(EventCategoryRepository eventCategoryRepository) {
        this.eventCategoryRepository = eventCategoryRepository;
    }
    @Override
    public ResponseEntity<List<EventCategory>> execute(Void input) {
        List<EventCategory> eventCategories = eventCategoryRepository.findAll().stream().toList();
        return ResponseEntity.ok().body(eventCategories);
    }
}
