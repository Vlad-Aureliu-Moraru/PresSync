package com.example.pressync.Common;

import com.example.pressync.Event.EventRepository;
import com.example.pressync.EventCategory.EventCategoryRepository;
import com.example.pressync.EventCategory.Model.EventCategory;
import com.example.pressync.EventCategory.Model.EventCategoryChangedEvent;
import com.example.pressync.EventCategory.Model.RepeatsOnSpecificDay;
import com.example.pressync.EventCategoryConfig.Model.EventCategoryConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DailyLoaderScheduler {
    private final EventRepository eventRepository;
    private final EventCategoryRepository eventCategoryRepository;
    private final TodayScheduleCache cache;
    private List<EventCategory> todayList = new ArrayList<>();
    @Scheduled(cron = "0 0 0  * * *")
    public void loadDailyEvents() {
        fillTodaySchedule();
    }
    @EventListener(ApplicationReadyEvent.class)
    public void onStart(){
        fillTodaySchedule();
    }


    public void fillTodaySchedule() {
        List<EventCategory> eventCategoryList = this.eventCategoryRepository.findAllWithConfigs();
        LocalDate baseDate = LocalDate.now();
        todayList.clear();
        for (EventCategory eventCategory : eventCategoryList) {
            if (shouldRunToday(eventCategory, baseDate)) {
                todayList.add(eventCategory);
            }
        }
        cache.updateList(todayList);

        System.out.println("***PRINTING TODAYLIST***");
        for (EventCategory eventCategory : todayList) {
            System.out.println(eventCategory.getName() + " id:" + eventCategory.getId() + " sT:" + eventCategory.getStartingTime());
        }
        System.out.println("***PRINTING TODAYLIST***");
    }

    private boolean shouldRunToday(EventCategory cat, LocalDate date) {
        if (!cat.getRepeatable()) {
            return cat.getSpecificDate() != null && cat.getSpecificDate().equals(date);
        }

        EventCategoryConfig config = cat.getCategoryConfig();
        if (config == null) return false;

        if (config.getRepeatsOnSpecificDay() != RepeatsOnSpecificDay.NO && config.getRepeatsOnSpecificDay() != null) {
            String todayShort = date.getDayOfWeek().name().substring(0, 3); // "MON"
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
    private void addToTodayList(EventCategory eventCategory) {
        LocalDate baseDate = LocalDate.now();
        if (shouldRunToday(eventCategory, baseDate)) {
            todayList.add(eventCategory);
        }
        cache.updateList(todayList);
    }
    @EventListener
    public void onCategoryCreated(EventCategoryChangedEvent event){
        System.out.println("Scheduler recieved update for "+ event.category().getName());
        addToTodayList(event.category());
    }
}
