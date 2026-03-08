package com.example.pressync.EventCategory.CommandHandlers;

import com.example.pressync.EventCategory.EventCategoryRepository;
import com.example.pressync.EventCategory.Model.EventCategory;
import com.example.pressync.EventCategory.Model.RepeatableType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Time;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateEventCategoryCommandTest {
  @Mock
  EventCategoryRepository eventCategoryRepository;
  @InjectMocks
  CreateEventCategoryCommand createEventCategoryCommand;
  @Test
    void testCreateEventCategoryCommandStartingTimeAfterEndTime() {
      EventCategory eventCategory = new EventCategory();
      eventCategory.setName("Test");
      eventCategory.setStartingTime(Time.valueOf("11:00:00"));
      eventCategory.setEndTime(Time.valueOf("10:00:00"));
      eventCategory.setAttendanceTimeStart(Time.valueOf("10:50:00"));
      eventCategory.setAttendanceDuration(11);

      RuntimeException exception =assertThrows(IllegalArgumentException.class, () -> createEventCategoryCommand.execute(eventCategory));
      assertTrue(exception.getMessage().equals("Invalid start time or end time"));
      verify(eventCategoryRepository,never()).save(any());
  }

  @Test
  void testCreateEventCategoryCommandAttendanceBeforeStartingTime() {
    EventCategory eventCategory = new EventCategory();
    eventCategory.setName("Test");
    eventCategory.setStartingTime(Time.valueOf("10:00:00"));
    eventCategory.setEndTime(Time.valueOf("11:00:00"));
    eventCategory.setAttendanceTimeStart(Time.valueOf("09:50:00"));
    eventCategory.setAttendanceDuration(11);

    RuntimeException exception =assertThrows(IllegalArgumentException.class, () -> createEventCategoryCommand.execute(eventCategory));
    assertTrue(exception.getMessage().equals("Invalid attendance start time"));
    verify(eventCategoryRepository,never()).save(any());
  }
  @Test
  void testCreateEventCategoryCommandCollision() {
    EventCategory existing = new EventCategory();
    existing.setRepeatableType(RepeatableType.DAILY);
    existing.setStartingTime(Time.valueOf("10:00:00"));
    existing.setEndTime(Time.valueOf("11:00:00"));

    when(eventCategoryRepository.findAll()).thenReturn(List.of(existing));

    EventCategory newCat = new EventCategory();
    newCat.setRepeatableType(RepeatableType.WEEKLY);
    newCat.setStartingTime(Time.valueOf("10:30:00"));
    newCat.setEndTime(Time.valueOf("11:30:00"));
    newCat.setAttendanceTimeStart(Time.valueOf("10:35:00"));
    newCat.setAttendanceDuration(10);

    RuntimeException exception = assertThrows(IllegalArgumentException.class,
            () -> createEventCategoryCommand.execute(newCat));

    assertEquals("Coliding with something", exception.getMessage());
    verify(eventCategoryRepository, never()).save(any());
  }

}