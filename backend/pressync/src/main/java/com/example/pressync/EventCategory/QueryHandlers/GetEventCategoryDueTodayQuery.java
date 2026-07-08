package com.example.pressync.EventCategory.QueryHandlers;

import com.example.pressync.Common.TodayScheduleCache;
import com.example.pressync.EventCategory.Model.DTO.EventCategoryGetDTO;
import com.example.pressync.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
@RequiredArgsConstructor
@Service
public class GetEventCategoryDueTodayQuery implements Query<Void, List<EventCategoryGetDTO>> {
    private final TodayScheduleCache todayScheduleCache;
    @Override
    public ResponseEntity<List<EventCategoryGetDTO>> execute(Void input) {
        List<EventCategoryGetDTO> dtos = todayScheduleCache.getEventCategoryList().stream()
                .map(EventCategoryGetDTO::new).toList();
        return ResponseEntity.ok(dtos);
    }
}
