package com.example.pressync.Event.CommandHandlers;

import com.example.pressync.Event.EventController;
import com.example.pressync.Event.EventRepository;
import com.example.pressync.Event.Model.Event;
import com.example.pressync.EventCategory.Model.EventCategory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Time;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CreateEventCommandTest {
@Mock
EventRepository eventRepository;
@InjectMocks
CreateEventCommand createEventCommand;
    @Test
    void testCreateEventCategoryCommand() {
        Event event = new Event();
        event.setEventCategory(null);
        event.setArchived(false);
        event.setArchived(false);


        RuntimeException exception =assertThrows(IllegalArgumentException.class, () ->createEventCommand.execute(event));
        assertTrue(exception.getMessage().contains("must"));
        verify(eventRepository,never()).save(any());
    }
}