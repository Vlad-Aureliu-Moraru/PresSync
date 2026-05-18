package com.example.pressync.Common;

import com.example.pressync.Event.EventRepository;
import com.example.pressync.EventCategory.EventCategoryRepository;
import com.example.pressync.EventCategory.Model.EventCategory;
import com.example.pressync.EventCategory.Model.RepeatableType;
import com.example.pressync.EventCategory.Model.RepeatsOnSpecificDay;
import com.example.pressync.EventCategoryConfig.Model.EventCategoryConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DailyLoaderSchedulerTest {

    @Mock
    private EventRepository eventRepository;
    @Mock
    private EventCategoryRepository eventCategoryRepository;

    @Test
    void fillTodayScheduleLoadsMatchingCategories() {
        TodayScheduleCache cache = new TodayScheduleCache();
        DailyLoaderScheduler scheduler = new DailyLoaderScheduler(eventRepository, eventCategoryRepository, cache);

        LocalDate today = LocalDate.now();

        EventCategory oneTimeToday = new EventCategory();
        oneTimeToday.setId(1);
        oneTimeToday.setRepeatable(false);
        oneTimeToday.setSpecificDate(today);

        EventCategory oneTimeTomorrow = new EventCategory();
        oneTimeTomorrow.setId(2);
        oneTimeTomorrow.setRepeatable(false);
        oneTimeTomorrow.setSpecificDate(today.plusDays(1));

        EventCategory dailyCategory = new EventCategory();
        dailyCategory.setId(3);
        dailyCategory.setRepeatable(true);
        EventCategoryConfig config = new EventCategoryConfig();
        config.setRepeatableType(RepeatableType.DAILY);
        config.setRepeatsOnSpecificDay(RepeatsOnSpecificDay.NO);
        config.setBaseDate(today.minusDays(1));
        dailyCategory.setCategoryConfig(config);

        when(eventCategoryRepository.findAllWithConfigs())
                .thenReturn(List.of(oneTimeToday, oneTimeTomorrow, dailyCategory));

        scheduler.fillTodaySchedule();

        assertEquals(2, cache.getEventCategoryList().size());
    }
}
