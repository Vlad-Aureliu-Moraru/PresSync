package com.example.pressync.EventCategory.CommandHandlers;

import com.example.pressync.Command;
import com.example.pressync.EventCategory.EventCategoryRepository;
import com.example.pressync.EventCategory.Model.EventCategory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.time.LocalDate;
import java.util.Calendar;

@Service
public class CreateEventCategoryCommand implements Command<EventCategory,String> {
    private EventCategoryRepository eventCategoryRepository;
    public CreateEventCategoryCommand(EventCategoryRepository eventCategoryRepository){
        this.eventCategoryRepository = eventCategoryRepository;
    }
    @Override
    public ResponseEntity<String> execute(EventCategory entity) {
        /*
         if the category is not repeatable it happends only once thus the values connected to repeating status should not be taken in account
     there should not be collisons (same time ;same day)
         HOW THE COLLISION SYSTEM WILL WORK :
         1.CHECK FOR BASE_DATE and REPEATABLE TYPE MATCHING
         2.  SOMETIMES DIFERENT BASE DATES WITH DIFFERENT REP TYPE COULD ALSO COLIDE
         2.1 EX: A DAILY EVENT COULD COLIDE WITH ANY TYPE OF REPEATABLE EVENT
         2.2 DAILY EVENTS SHOULD HAVE A DEDICATED WINDOW THAT DOESNT OVERLAP WITH ALL EVENTS
         3. IF COLISION DETECTED THROW ERROR
         */


        if(entity.getStartingTime() == null||entity.getEndTime() == null){
            throw new IllegalArgumentException("Start and end time are required");
        }
        Time startingTime = entity.getStartingTime();
        Time endTime = entity.getEndTime();
        Time atendenceStartingTime = entity.getAttendanceTimeStart();
        Integer atendenceDuration = entity.getAttendanceDuration();

        if (atendenceStartingTime.after(endTime) || atendenceStartingTime.before(startingTime)){
            throw new IllegalArgumentException("Attendance time must be between start and end time.");
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(atendenceStartingTime);
        calendar.add(Calendar.MINUTE, atendenceDuration);
        Time attendanceEndingTime = new Time(calendar.getTimeInMillis());
        entity.setBaseDate(LocalDate.now());
        if (attendanceEndingTime.after(endTime)){
            throw new IllegalArgumentException("Attendance end time must be before the event start time.");
        }


        eventCategoryRepository.save(entity);
        return ResponseEntity.ok().body(entity.toString());
    }
}
