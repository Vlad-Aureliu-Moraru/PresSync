package com.example.pressync.EventCategory.CommandHandlers;

import com.example.pressync.Command;
import com.example.pressync.EventCategory.EventCategoryRepository;
import com.example.pressync.EventCategory.Model.EventCategory;
import com.example.pressync.EventCategory.Model.EventCategoryChangedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteEventCategoryCommand implements Command<Integer, String> {
    private final EventCategoryRepository eventCategoryRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    @Override
    public ResponseEntity<String> execute(Integer entity) {
        EventCategory found = eventCategoryRepository.findById(entity)
                .orElseThrow(() -> new IllegalArgumentException("Event category with id " + entity + " does not exist."));
        eventCategoryRepository.delete(found);
        applicationEventPublisher.publishEvent(new EventCategoryChangedEvent(found));
        return ResponseEntity.ok().build();
    }
}
