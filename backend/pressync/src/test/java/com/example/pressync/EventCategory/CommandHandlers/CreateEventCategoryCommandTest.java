package com.example.pressync.EventCategory.CommandHandlers;

import com.example.pressync.EventCategory.EventCategoryRepository;
import com.example.pressync.EventCategory.Model.EventCategory;
import com.example.pressync.EventCategory.Model.RepeatableType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.pressync.EventCategory.Model.DTO.CreateEventCategoryRequest;
import com.example.pressync.EventCategoryConfig.EventCategoryConfigRepository;
import com.example.pressync.EventCategoryConfig.Model.EventCategoryConfig;
import org.springframework.context.ApplicationEventPublisher;

import java.sql.Time;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateEventCategoryCommandTest {
  @Mock
  EventCategoryRepository eventCategoryRepository;
  @Mock
  EventCategoryConfigRepository eventCategoryConfigRepository;
  @Mock
  ApplicationEventPublisher applicationEventPublisher;
  @InjectMocks
  CreateEventCategoryCommand createEventCategoryCommand;

  @Test
  void testCreateEventCategoryCommandStartingTimeAfterEndTime() {
      CreateEventCategoryRequest request = new CreateEventCategoryRequest(
              "Test",
              Time.valueOf("11:00:00"),
              Time.valueOf("10:00:00"),
              Time.valueOf("10:50:00"),
              11,
              false,
              null,
              null,
              null,
              LocalDate.now()
      );

      RuntimeException exception = assertThrows(IllegalArgumentException.class, () -> createEventCategoryCommand.execute(request));
      assertTrue(exception.getMessage().equals("Invalid start time or end time"));
      verify(eventCategoryRepository, never()).save(any());
  }

  @Test
  void testCreateEventCategoryCommandAttendanceBeforeStartingTime() {
      CreateEventCategoryRequest request = new CreateEventCategoryRequest(
              "Test",
              Time.valueOf("10:00:00"),
              Time.valueOf("11:00:00"),
              Time.valueOf("09:50:00"),
              11,
              false,
              null,
              null,
              null,
              LocalDate.now()
      );

      RuntimeException exception = assertThrows(IllegalArgumentException.class, () -> createEventCategoryCommand.execute(request));
      assertTrue(exception.getMessage().equals("Invalid attendance start time"));
      verify(eventCategoryRepository, never()).save(any());
  }

  @Test
  void testCreateEventCategoryCommandCollision() {
    EventCategory existing = new EventCategory();
    existing.setStartingTime(Time.valueOf("10:00:00"));
    existing.setEndTime(Time.valueOf("11:00:00"));
    existing.setRepeatable(true);
    
    EventCategoryConfig config = new EventCategoryConfig();
    config.setRepeatableType(RepeatableType.DAILY);
    existing.setCategoryConfig(config);

    when(eventCategoryRepository.findAll()).thenReturn(List.of(existing));

    CreateEventCategoryRequest newCatRequest = new CreateEventCategoryRequest(
            "Test",
            Time.valueOf("10:30:00"),
            Time.valueOf("11:30:00"),
            Time.valueOf("10:35:00"),
            10,
            true,
            null,
            RepeatableType.WEEKLY,
            null,
            LocalDate.now()
    );

    RuntimeException exception = assertThrows(IllegalArgumentException.class,
            () -> createEventCategoryCommand.execute(newCatRequest));

    assertEquals("Coliding with something", exception.getMessage());
    verify(eventCategoryRepository, never()).save(any());
  }

}