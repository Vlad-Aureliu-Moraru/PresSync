package com.example.pressync.Event.CommandHandlers;

import com.example.pressync.Event.EventRepository;
import com.example.pressync.Event.Model.Event;
import com.example.pressync.Event.Model.EventPutDTO;
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
class UpdateEventCommandTest {

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private UpdateEventCommand updateEventCommand;

    private EventPutDTO eventPutDTO;
    private Event event;

    @BeforeEach
    void setUp() {
        event = new Event();
        event.setId(1);
        
        eventPutDTO = new EventPutDTO();
        eventPutDTO.setId(1);
        eventPutDTO.setEvent(event);
    }

    @Test
    void testUpdateEventSuccess() {
        when(eventRepository.findById(1)).thenReturn(Optional.of(event));
        when(eventRepository.save(any(Event.class))).thenReturn(event);

        ResponseEntity<String> response = updateEventCommand.execute(eventPutDTO);

        assertEquals(200, response.getStatusCode().value());
        verify(eventRepository, times(1)).save(any(Event.class));
    }

    @Test
    void testUpdateEventNotFound() {
        when(eventRepository.findById(1)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> updateEventCommand.execute(eventPutDTO));

        assertEquals("Event with id 1 does not exist", exception.getMessage());
        verify(eventRepository, never()).save(any());
    }
}
