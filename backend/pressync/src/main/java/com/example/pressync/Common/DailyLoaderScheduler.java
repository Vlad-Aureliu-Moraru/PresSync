package com.example.pressync.Common;

import com.example.pressync.Event.EventRepository;
import com.example.pressync.Event.Model.Event;
import com.example.pressync.EventCategory.EventCategoryRepository;
import com.example.pressync.EventCategory.Model.EventCategory;
import com.example.pressync.EventCategory.Model.EventCategoryChangedEvent;
import com.example.pressync.EventCategory.Model.RepeatableType;
import com.example.pressync.EventCategory.Model.RepeatsOnSpecificDay;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
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
        List<EventCategory> eventCategoryList = this.eventCategoryRepository.findAll();
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
            return cat.getBaseDate().equals(date);
        }

        if (cat.getRepeatsOnSpecificDay() != RepeatsOnSpecificDay.NO) {
            String todayShort = date.getDayOfWeek().name().substring(0, 3); // "MON"
            if (!todayShort.equals(cat.getRepeatsOnSpecificDay().name())) {
                return false;
            }
        }

        long daysBetween = ChronoUnit.DAYS.between(cat.getBaseDate(), date);
        if (daysBetween < 0) return false;

        return switch (cat.getRepeatableType()) {
            case DAILY -> true;
            case WEEKLY -> daysBetween % 7 == 0;
            case BIWEEKLY -> daysBetween % 14 == 0;
            case MONTHLY -> date.getDayOfMonth() == cat.getBaseDate().getDayOfMonth();
            case YEARLY -> date.getMonth() == cat.getBaseDate().getMonth() &&
                    date.getDayOfMonth() == cat.getBaseDate().getDayOfMonth();
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
