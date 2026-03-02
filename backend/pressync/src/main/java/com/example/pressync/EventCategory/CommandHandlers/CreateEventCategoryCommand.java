package com.example.pressync.EventCategory.CommandHandlers;

import com.example.pressync.Command;
import com.example.pressync.EventCategory.EventCategoryRepository;
import com.example.pressync.EventCategory.Model.EventCategory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.util.Calendar;

@Service
public class CreateEventCategoryCommand implements Command<EventCategory,String> {
    private EventCategoryRepository eventCategoryRepository;
    public CreateEventCategoryCommand(EventCategoryRepository eventCategoryRepository){
        this.eventCategoryRepository = eventCategoryRepository;
    }
    @Override
    public ResponseEntity<String> execute(EventCategory entity) {
        Time startingTime = entity.getStartingTime();
        Time endTime = entity.getEndTime();
        Time atendenceStartingTime = entity.getAttendanceTimeStart();
        int atendenceDuration = entity.getAttendanceDuration();

        if (atendenceStartingTime.after(endTime) || atendenceStartingTime.before(startingTime)){
            throw new IllegalArgumentException("Attendance time must be between start and end time.");
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(atendenceStartingTime);
        calendar.add(Calendar.MINUTE, atendenceDuration);
        Time attendanceEndingTime = new Time(calendar.getTimeInMillis());

        if (attendanceEndingTime.after(endTime)){
            throw new IllegalArgumentException("Attendance end time must be before the event start time.");
        }


        eventCategoryRepository.save(entity);
        return ResponseEntity.ok().body(entity.toString());
    }
}
