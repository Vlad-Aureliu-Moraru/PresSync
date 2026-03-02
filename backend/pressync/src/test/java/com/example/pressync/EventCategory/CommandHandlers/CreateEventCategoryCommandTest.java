package com.example.pressync.EventCategory.CommandHandlers;

import com.example.pressync.EventCategory.EventCategoryRepository;
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
class CreateEventCategoryCommandTest {
  @Mock
  EventCategoryRepository eventCategoryRepository;
  @InjectMocks
  CreateEventCategoryCommand createEventCategoryCommand;
  @Test
    void testCreateEventCategoryCommand() {
      EventCategory eventCategory = new EventCategory();
      eventCategory.setName("Test");
      eventCategory.setStartingTime(Time.valueOf("10:00:00"));
      eventCategory.setEndTime(Time.valueOf("11:00:00"));
      eventCategory.setAttendanceTimeStart(Time.valueOf("10:50:00"));
      eventCategory.setAttendanceDuration(11);

      RuntimeException exception =assertThrows(IllegalArgumentException.class, () -> createEventCategoryCommand.execute(eventCategory));
      assertTrue(exception.getMessage().contains("must"));
      verify(eventCategoryRepository,never()).save(any());
  }
}