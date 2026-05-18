package com.example.pressync.Common;

import com.example.pressync.Event.EventRepository;
import com.example.pressync.Event.Model.Event;
import com.example.pressync.EventCategory.Model.EventCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Time;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MinuteEventSchedulerTest {

    @Mock
    private EventRepository eventRepository;

    private TodayScheduleCache cache;
    private MinuteEventScheduler scheduler;
    private LocalTime now;

    @BeforeEach
    void setUp() {
        cache = new TodayScheduleCache();
        scheduler = new MinuteEventScheduler(eventRepository, cache);
        now = LocalTime.now().truncatedTo(ChronoUnit.MINUTES);
    }

    @Test
    void startEventWhenStartTimeMatchesNow() {
        EventCategory cat = new EventCategory();
        cat.setId(1);
        cat.setName("Test Category");
        cat.setStartingTime(Time.valueOf(now));
        cat.setEndTime(Time.valueOf(now.plusMinutes(1)));

        cache.updateList(List.of(cat));

        scheduler.autoGenerateEvents();

        verify(eventRepository, times(1)).archiveByCategory(1);
        verify(eventRepository, times(1)).save(any(Event.class));
    }

    @Test
    void endEventWhenEndTimeMatchesNow() {
        EventCategory cat = new EventCategory();
        cat.setId(1);
        cat.setName("Test Category");
        cat.setStartingTime(Time.valueOf(now.minusMinutes(1)));
        cat.setEndTime(Time.valueOf(now));

        cache.updateList(List.of(cat));

        scheduler.autoGenerateEvents();

        verify(eventRepository, times(1)).archiveByCategory(1);
        verify(eventRepository, never()).save(any(Event.class));
    }

    @Test
    void startSkippedWhenStartEqualsEnd() {
        EventCategory cat = new EventCategory();
        cat.setId(1);
        cat.setName("Test Category");
        cat.setStartingTime(Time.valueOf(now));
        cat.setEndTime(Time.valueOf(now));

        cache.updateList(List.of(cat));

        scheduler.autoGenerateEvents();

        verify(eventRepository, times(1)).archiveByCategory(1);
        verify(eventRepository, never()).save(any(Event.class));
    }

    @Test
    void noActionWhenTimesDoNotMatch() {
        EventCategory cat = new EventCategory();
        cat.setId(1);
        cat.setName("Test Category");
        cat.setStartingTime(Time.valueOf(now.plusHours(2)));
        cat.setEndTime(Time.valueOf(now.plusHours(3)));

        cache.updateList(List.of(cat));

        scheduler.autoGenerateEvents();

        verify(eventRepository, never()).archiveByCategory(anyInt());
        verify(eventRepository, never()).save(any(Event.class));
    }

    @Test
    void errorInOneCategoryDoesNotStopOthers() {
        EventCategory badCat = new EventCategory();
        badCat.setId(1);
        badCat.setName("Bad Category");
        badCat.setStartingTime(null); // will cause NullPointerException

        EventCategory goodCat = new EventCategory();
        goodCat.setId(2);
        goodCat.setName("Good Category");
        goodCat.setStartingTime(Time.valueOf(now));
        goodCat.setEndTime(Time.valueOf(now.plusMinutes(1)));

        cache.updateList(List.of(badCat, goodCat));

        assertDoesNotThrow(() -> scheduler.autoGenerateEvents());

        verify(eventRepository, times(1)).archiveByCategory(2);
        verify(eventRepository, times(1)).save(any(Event.class));
    }
}
