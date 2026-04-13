package com.example.pressync.EventCategory.CommandHandlers;

import com.example.pressync.EventCategory.EventCategoryRepository;
import com.example.pressync.EventCategory.Model.EventCategory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.net.http.HttpResponse;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteEventCategoryCommandTest {

    @Mock
    private EventCategoryRepository eventCategoryRepository;

    @InjectMocks
    private DeleteEventCategoryCommand deleteEventCategoryCommand;

    @Test
    void testDeleteEventCategorySuccess() {
        int id = 1;
        EventCategory category = new EventCategory();
        category.setId(id);
        
        when(eventCategoryRepository.findById(id)).thenReturn(Optional.of(category));

        @SuppressWarnings("rawtypes")
        ResponseEntity<HttpResponse> response = deleteEventCategoryCommand.execute(id);

        assertEquals(200, response.getStatusCode().value());
        verify(eventCategoryRepository, times(1)).delete(category);
    }

    @Test
    void testDeleteEventCategoryNotFound() {
        int id = 1;
        when(eventCategoryRepository.findById(id)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> deleteEventCategoryCommand.execute(id));

        assertTrue(exception.getMessage().contains("does not exist"));
        verify(eventCategoryRepository, never()).delete(any());
    }
}
