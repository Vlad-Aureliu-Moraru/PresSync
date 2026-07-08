package com.example.pressync.Common;

import com.example.pressync.EventCategory.Model.EventCategory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * In-memory cache holding the list of event categories scheduled for today.
 * Populated by {@link DailyLoaderScheduler} at midnight and on category changes.
 * Uses a {@code volatile} field with an unmodifiable copy on every update
 * to provide safe publication across scheduler and controller threads.
 */
@Component
public class TodayScheduleCache {
    private volatile List<EventCategory> eventCategoryList = new ArrayList<>();

    public List<EventCategory> getEventCategoryList() {
        return eventCategoryList;
    }

    public void updateList(List<EventCategory> newList) {
        this.eventCategoryList = Collections.unmodifiableList(new ArrayList<>(newList));
    }
}
