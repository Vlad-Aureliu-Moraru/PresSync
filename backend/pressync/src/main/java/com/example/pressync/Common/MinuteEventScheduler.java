package com.example.pressync.Common;

import com.example.pressync.Event.EventRepository;
import com.example.pressync.Event.Model.Event;
import com.example.pressync.EventCategory.Model.EventCategory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class MinuteEventScheduler {
    private final EventRepository eventRepository;
    private final TodayScheduleCache todayScheduleCache;

    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void autoGenerateEvents() {
        System.out.println("Auto generate events"+ todayScheduleCache.getEventCategoryList().size());
        LocalTime now = LocalTime.now().truncatedTo(ChronoUnit.MINUTES);
        for (EventCategory cat : todayScheduleCache.getEventCategoryList()){
            LocalTime startTime = cat.getStartingTime().toLocalTime().truncatedTo(ChronoUnit.MINUTES);
            LocalTime endTime = cat.getEndTime().toLocalTime().truncatedTo(ChronoUnit.MINUTES);

            if (startTime.equals(now)){
                startEvent(cat);
            }
            if (endTime.equals(now)){
                endEvent(cat);
            }
        }
    }
    private void startEvent(EventCategory cat){
        eventRepository.archiveByCategory(cat.getId());
        Event event = new Event();
        event.setEventCategory(cat);
        event.setActive(true);
        event.setArchived(false);
        eventRepository.save(event);
        System.out.println("Started Event: " + cat.getName() + " at " + LocalTime.now());
    }

    private void endEvent(EventCategory cat){
        eventRepository.archiveByCategory(cat.getId());
        System.out.println("Archived Event: " + cat.getName() + " at " + LocalTime.now());

    }
}
