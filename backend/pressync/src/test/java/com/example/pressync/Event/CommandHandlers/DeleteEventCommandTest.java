package com.example.pressync.Event.CommandHandlers;

import com.example.pressync.Event.EventRepository;
import com.example.pressync.Event.Model.Event;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteEventCommandTest {

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private DeleteEventCommand deleteEventCommand;

    @Test
    void testDeleteEventSuccess() {
        int id = 1;
        Event event = new Event();
        event.setId(id);
        
        when(eventRepository.findById(id)).thenReturn(Optional.of(event));

        ResponseEntity<String> response = deleteEventCommand.execute(id);

        assertEquals(200, response.getStatusCode().value());
        verify(eventRepository, times(1)).delete(event);
    }

    @Test
    void testDeleteEventNotFound() {
        int id = 1;
        when(eventRepository.findById(id)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> deleteEventCommand.execute(id));

        assertEquals("Event with id 1 does not exist", exception.getMessage());
        verify(eventRepository, never()).delete(any());
    }
}
