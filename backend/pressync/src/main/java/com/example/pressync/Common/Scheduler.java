package com.example.pressync.Common;

import com.example.pressync.Event.EventRepository;
import com.example.pressync.Event.Model.Event;
import com.example.pressync.EventCategory.EventCategoryRepository;
import com.example.pressync.EventCategory.Model.EventCategory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class Scheduler {
    private final EventRepository eventRepository;
    private final EventCategoryRepository eventCategoryRepository;

    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void autoGenerateEvents() {
        LocalDateTime now = LocalDateTime.now();
        int hour = now.getHour();
        int minute = now.getMinute();
        List<EventCategory> startingEvents = eventCategoryRepository.findByStartingTimeNative(hour, minute);
        System.out.println("Auto generate events for "+startingEvents.size()+" events");
        if (!startingEvents.isEmpty()) {
            eventRepository.deactivateAllEvents();

            for (EventCategory cat :startingEvents) {
                Event newEvent = new Event();
                newEvent.setEventCategory(cat);
                newEvent.setActive(true);
                newEvent.setArchived(false);
                eventRepository.save(newEvent);
                System.out.println("Started Event for Category: " + cat.getName());
            }
        }

        List<Event> toClose = eventRepository.findExpiredEvents(hour, minute);

        for (Event event : toClose) {
            event.setActive(false);
            event.setArchived(true);
            eventRepository.save(event);
            System.out.println("Archived Event: " + event.getId());
        }

    }
}
