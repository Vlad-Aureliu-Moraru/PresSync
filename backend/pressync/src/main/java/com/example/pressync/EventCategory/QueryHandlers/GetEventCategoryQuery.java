package com.example.pressync.EventCategory.QueryHandlers;

import com.example.pressync.EventCategory.EventCategoryRepository;
import com.example.pressync.EventCategory.Model.DTO.EventCategoryGetDTO;
import com.example.pressync.Query;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class GetEventCategoryQuery implements Query<Integer, EventCategoryGetDTO> {
    private final EventCategoryRepository eventCategoryRepository;
    public GetEventCategoryQuery(EventCategoryRepository eventCategoryRepository) {
        this.eventCategoryRepository = eventCategoryRepository;
    }
    @Override
    @Cacheable(value = "eventCategories", key = "#input")
    public ResponseEntity<EventCategoryGetDTO> execute(Integer input) {
        EventCategoryGetDTO dto = eventCategoryRepository.findByIdWithDetails(input)
                .map(EventCategoryGetDTO::new)
                .orElseThrow(() -> new IllegalArgumentException("Event category with id " + input + " does not exist."));
        return ResponseEntity.ok(dto);
    }
}
