package com.example.pressync.Common;

import com.example.pressync.Event.EventRepository;
import com.example.pressync.Event.Model.Event;
import com.example.pressync.EventCategory.Model.EventCategory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

/**
 * Scheduled service that runs every minute to start and end events for categories
 * that are due today. Uses {@link TodayScheduleCache} to avoid querying the database
 * on every tick. Starts a new event when the current time matches the category's
 * starting time, and archives it when the end time is reached.
 */
@Service
@RequiredArgsConstructor
public class MinuteEventScheduler {
    private static final Logger log = LoggerFactory.getLogger(MinuteEventScheduler.class);
    private final EventRepository eventRepository;
    private final TodayScheduleCache todayScheduleCache;

    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void autoGenerateEvents() {
        log.debug("Auto-generating events for {} categories", todayScheduleCache.getEventCategoryList().size());
        LocalTime now = LocalTime.now().truncatedTo(ChronoUnit.MINUTES);
        for (EventCategory cat : todayScheduleCache.getEventCategoryList()){
            try {
                LocalTime startTime = cat.getStartingTime().toLocalTime().truncatedTo(ChronoUnit.MINUTES);
                LocalTime endTime = cat.getEndTime().toLocalTime().truncatedTo(ChronoUnit.MINUTES);

                if (startTime.equals(now) && !startTime.equals(endTime)){
                    startEvent(cat);
                }
                if (endTime.equals(now)){
                    endEvent(cat);
                }
            } catch (Exception e) {
                log.error("Error processing category {}: {}", cat.getName(), e.getMessage(), e);
            }
        }
    }
    private void startEvent(EventCategory cat){
        eventRepository.archiveByCategory(cat.getId());
        Event event = new Event();
        event.setEventCategory(cat);
        event.setActive(true);
        event.setArchived(false);
        event.setDate(LocalDate.now());
        eventRepository.save(event);
        log.info("Started Event: {} at {}", cat.getName(), LocalTime.now());
    }

    private void endEvent(EventCategory cat){
        eventRepository.archiveByCategory(cat.getId());
        log.info("Archived Event: {} at {}", cat.getName(), LocalTime.now());
    }
}
