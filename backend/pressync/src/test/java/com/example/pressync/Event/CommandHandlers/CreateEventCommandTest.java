package com.example.pressync.Event.CommandHandlers;

import com.example.pressync.Event.EventRepository;
import com.example.pressync.Event.Model.Event;
import com.example.pressync.EventCategory.EventCategoryRepository;
import com.example.pressync.EventCategory.Model.EventCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateEventCommandTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private EventCategoryRepository eventCategoryRepository;

    @InjectMocks
    private CreateEventCommand createEventCommand;

    private Event event;
    private EventCategory category;

    @BeforeEach
    void setUp() {
        category = new EventCategory();
        category.setId(1);

        event = new Event();
        event.setEventCategory(category);
        event.setActive(true);
    }

    @Test
    void testCreateEventSuccess() {
        when(eventCategoryRepository.findById(1)).thenReturn(Optional.of(category));
        when(eventRepository.save(any(Event.class))).thenReturn(event);

        ResponseEntity<String> response = createEventCommand.execute(event);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Success", response.getBody());
        verify(eventRepository, times(1)).save(event);
    }

    @Test
    void testCreateEventCategoryNotFound() {
        when(eventCategoryRepository.findById(1)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> createEventCommand.execute(event));

        assertEquals("Event Category Not Found", exception.getMessage());
        verify(eventRepository, never()).save(any());
    }

    @Test
    void testCreateEventDatabaseError() {
        when(eventCategoryRepository.findById(1)).thenReturn(Optional.of(category));
        when(eventRepository.save(any(Event.class))).thenThrow(new RuntimeException("DB Error"));

        ResponseEntity<String> response = createEventCommand.execute(event);

        assertEquals(400, response.getStatusCode().value());
        verify(eventRepository, times(1)).save(any());
    }
}