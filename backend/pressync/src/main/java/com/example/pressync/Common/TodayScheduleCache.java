package com.example.pressync.Common;

import com.example.pressync.Event.EventRepository;
import com.example.pressync.EventCategory.EventCategoryRepository;
import com.example.pressync.EventCategory.Model.EventCategory;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Getter
public class TodayScheduleCache {
    private final List<EventCategory> eventCategoryList = new ArrayList<>();

    public void updateList(List<EventCategory> eventCategoryList) {
        this.eventCategoryList.clear();
        this.eventCategoryList.addAll(eventCategoryList);

    }
}
