package com.example.pressync.EventCategory.QueryHandlers;

import com.example.pressync.Common.TodayScheduleCache;
import com.example.pressync.Event.Model.Event;
import com.example.pressync.EventCategory.Model.EventCategory;
import com.example.pressync.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
@RequiredArgsConstructor
@Service
public class GetEventCategoryDueTodayQuery implements Query<Void, List<EventCategory>> {
    public final TodayScheduleCache todayScheduleCache;
    @Override
    public ResponseEntity<List<EventCategory>> execute(Void input) {
        return ResponseEntity.ok().body(todayScheduleCache.getEventCategoryList());
    }
}
