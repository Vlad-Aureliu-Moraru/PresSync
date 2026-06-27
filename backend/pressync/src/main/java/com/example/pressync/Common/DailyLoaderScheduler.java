package com.example.pressync.Common;

import com.example.pressync.Event.EventRepository;
import com.example.pressync.EventCategory.EventCategoryRepository;
import com.example.pressync.EventCategory.Model.EventCategory;
import com.example.pressync.EventCategory.Model.EventCategoryChangedEvent;
import com.example.pressync.EventCategory.Model.RepeatsOnSpecificDay;
import com.example.pressync.EventCategoryConfig.Model.EventCategoryConfig;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DailyLoaderScheduler {
    private static final Logger log = LoggerFactory.getLogger(DailyLoaderScheduler.class);
    private final EventRepository eventRepository;
    private final EventCategoryRepository eventCategoryRepository;
    private final TodayScheduleCache cache;

    @Scheduled(cron = "0 0 0  * * *")
    @Transactional
    public void loadDailyEvents() {
        fillTodaySchedule();
    }

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void onStart(){
        fillTodaySchedule();
    }

    public void fillTodaySchedule() {
        eventRepository.archiveAllActiveEvents();
        List<EventCategory> eventCategoryList = this.eventCategoryRepository.findAllWithConfigs();
        LocalDate baseDate = LocalDate.now();
        List<EventCategory> todayEvents = new ArrayList<>();
        for (EventCategory eventCategory : eventCategoryList) {
            if (shouldRunToday(eventCategory, baseDate)) {
                todayEvents.add(eventCategory);
            }
        }
        cache.updateList(todayEvents);

        log.info("Today's schedule loaded ({} events)", todayEvents.size());
        for (EventCategory eventCategory : todayEvents) {
            log.info("  {} id:{} startTime:{}", eventCategory.getName(), eventCategory.getId(), eventCategory.getStartingTime());
        }
    }

    private boolean shouldRunToday(EventCategory cat, LocalDate date) {
        if (Boolean.FALSE.equals(cat.getRepeatable())) {
            return cat.getSpecificDate() != null && cat.getSpecificDate().equals(date);
        }

        EventCategoryConfig config = cat.getCategoryConfig();
        if (config == null) return false;

        if (config.getRepeatsOnSpecificDay() != RepeatsOnSpecificDay.NO && config.getRepeatsOnSpecificDay() != null) {
            String todayShort = date.getDayOfWeek().name().substring(0, 3);
            if (!todayShort.equals(config.getRepeatsOnSpecificDay().name())) {
                return false;
            }
        }

        long daysBetween = ChronoUnit.DAYS.between(config.getBaseDate(), date);
        if (daysBetween < 0) return false;

        return switch (config.getRepeatableType()) {
            case DAILY -> true;
            case WEEKLY -> daysBetween % 7 == 0;
            case BIWEEKLY -> daysBetween % 14 == 0;
            case MONTHLY -> date.getDayOfMonth() == config.getBaseDate().getDayOfMonth();
            case YEARLY -> date.getMonth() == config.getBaseDate().getMonth() &&
                    date.getDayOfMonth() == config.getBaseDate().getDayOfMonth();
            default -> false;
        };
    }

    @EventListener
    @Transactional
    public void onCategoryChanged(EventCategoryChangedEvent event){
        log.info("Scheduler received change for {}", event.category().getName());
        fillTodaySchedule();
    }
}
