package com.example.pressync.EventCategory.CommandHandlers;

import com.example.pressync.EventCategory.EventCategoryRepository;
import com.example.pressync.EventCategory.Model.EventCategory;
import com.example.pressync.EventCategory.Model.EventCategoryUpdateDTO;
import com.example.pressync.EventCategory.Model.DTO.UpdateEventCategoryRequest;
import com.example.pressync.EventCategoryConfig.EventCategoryConfigRepository;
import com.example.pressync.EventCategoryConfig.Model.EventCategoryConfig;
import com.example.pressync.EventCategory.Model.RepeatableType;
import com.example.pressync.EventCategory.Model.RepeatsOnSpecificDay;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.sql.Time;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateEventCategoryCommandTest {

    @Mock
    private EventCategoryRepository eventCategoryRepository;
    @Mock
    private EventCategoryConfigRepository eventCategoryConfigRepository;

    @InjectMocks
    private UpdateEventCategoryCommand updateEventCategoryCommand;

    private EventCategory eventCategory;
    private UpdateEventCategoryRequest request;
    private EventCategoryUpdateDTO updateDTO;

    @BeforeEach
    void setUp() {
        eventCategory = new EventCategory();
        eventCategory.setId(1);
        eventCategory.setName("Old Name");
        eventCategory.setStartingTime(Time.valueOf("10:00:00"));
        eventCategory.setEndTime(Time.valueOf("11:00:00"));
        eventCategory.setAttendanceTimeStart(Time.valueOf("10:05:00"));
        eventCategory.setAttendanceDuration(10);
        eventCategory.setRepeatable(false);
        eventCategory.setSpecificDate(LocalDate.now());

        request = new UpdateEventCategoryRequest(
                "New Name",
                Time.valueOf("10:00:00"),
                Time.valueOf("11:00:00"),
                Time.valueOf("10:05:00"),
                10,
                false,
                null,
                null,
                null,
                LocalDate.now()
        );

        updateDTO = new EventCategoryUpdateDTO(1, request);
    }

    @Test
    void testUpdateEventCategorySuccess() {
        when(eventCategoryRepository.findById(1)).thenReturn(Optional.of(eventCategory));
        when(eventCategoryRepository.findAll()).thenReturn(Collections.emptyList());

        ResponseEntity<String> response = updateEventCategoryCommand.execute(updateDTO);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("New Name", eventCategory.getName());
        verify(eventCategoryRepository, times(1)).save(eventCategory);
    }

    @Test
    void testUpdateEventCategoryNotFound() {
        when(eventCategoryRepository.findById(1)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> updateEventCategoryCommand.execute(updateDTO));

        assertTrue(exception.getMessage().contains("does not exist"));
        verify(eventCategoryRepository, never()).save(any());
    }

    @Test
    void testUpdateEventCategoryInvalidTimes() {
        UpdateEventCategoryRequest invalidRequest = new UpdateEventCategoryRequest(
                "Test",
                Time.valueOf("12:00:00"),
                Time.valueOf("11:00:00"),
                Time.valueOf("12:05:00"),
                10,
                false,
                null,
                null,
                null,
                LocalDate.now()
        );
        EventCategoryUpdateDTO invalidDTO = new EventCategoryUpdateDTO(1, invalidRequest);

        when(eventCategoryRepository.findById(1)).thenReturn(Optional.of(eventCategory));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> updateEventCategoryCommand.execute(invalidDTO));

        assertEquals("Invalid start time or end time", exception.getMessage());
        verify(eventCategoryRepository, never()).save(any());
    }

    @Test
    void testUpdateEventCategoryCollision() {
        EventCategory existing = new EventCategory();
        existing.setId(2);
        existing.setStartingTime(Time.valueOf("10:30:00"));
        existing.setEndTime(Time.valueOf("11:30:00"));
        existing.setRepeatable(false);
        existing.setSpecificDate(LocalDate.now());

        when(eventCategoryRepository.findById(1)).thenReturn(Optional.of(eventCategory));
        when(eventCategoryRepository.findAll()).thenReturn(Collections.singletonList(existing));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> updateEventCategoryCommand.execute(updateDTO));

        assertEquals("Coliding with something", exception.getMessage());
        verify(eventCategoryRepository, never()).save(any());
    }
}
