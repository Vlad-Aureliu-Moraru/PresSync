package com.example.pressync.EventCategory.QueryHandlers;

import com.example.pressync.EventCategory.EventCategoryRepository;
import com.example.pressync.EventCategory.Model.DTO.EventCategoryGetDTO;
import com.example.pressync.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class GetAllEventCategoriesQuerry implements Query<Void, List<EventCategoryGetDTO>> {
    private final EventCategoryRepository eventCategoryRepository;
    public GetAllEventCategoriesQuerry(EventCategoryRepository eventCategoryRepository) {
        this.eventCategoryRepository = eventCategoryRepository;
    }
    @Override
    public ResponseEntity<List<EventCategoryGetDTO>> execute(Void input) {
        List<EventCategoryGetDTO> dtos = eventCategoryRepository.findAllWithConfigs().stream()
                .map(EventCategoryGetDTO::new).toList();
        return ResponseEntity.ok(dtos);
    }
}
