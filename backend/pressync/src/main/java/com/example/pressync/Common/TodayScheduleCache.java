package com.example.pressync.Common;

import com.example.pressync.EventCategory.Model.EventCategory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
