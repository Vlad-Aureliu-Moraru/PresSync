package com.example.pressync.EventCategory.CommandHandlers;

import com.example.pressync.Command;
import com.example.pressync.EventCategory.EventCategoryRepository;
import com.example.pressync.EventCategory.Model.EventCategory;
import com.example.pressync.EventCategory.Model.RepeatableType;
import com.example.pressync.EventCategory.Model.RepeatsOnSpecificDay;
import jakarta.transaction.Transactional;
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
    @Transactional
    public ResponseEntity<String> execute(EventCategory entity) {

        entity.setBaseDate(LocalDate.now());
        checkValidity(entity);
        if (isColidingWithSomething(entity)){
            throw new IllegalArgumentException("Coliding with something");
        };


        eventCategoryRepository.save(entity);
        return ResponseEntity.ok().body(entity.toString());
    }
    private void checkValidity(EventCategory entity){
        Time startingTime = entity.getStartingTime();
        Time endTime = entity.getEndTime();
        if (startingTime.after(endTime)||startingTime.equals(endTime)){
            throw new IllegalArgumentException("Invalid start time or end time");
        }
        Time atendenceStartingTime = entity.getAttendanceTimeStart();
        if (atendenceStartingTime.before(startingTime)||atendenceStartingTime.after(endTime)){
            throw new IllegalArgumentException("Invalid attendance start time");
        }
        Integer atendenceDuration = entity.getAttendanceDuration();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(atendenceStartingTime);
        calendar.add(Calendar.MINUTE, atendenceDuration);
        if (calendar.getTime().after(endTime)){
            throw new IllegalArgumentException("Invalid attendance duration");
        }

    }

    private boolean isColidingWithSomething(EventCategory newCat) {
        for (EventCategory existing : eventCategoryRepository.findAll()) {
            if (newCat.getId() != null && newCat.getId().equals(existing.getId())) continue;

            if (doDaysOverlap(newCat, existing)) {
                if (newCat.getStartingTime().before(existing.getEndTime()) &&
                        newCat.getEndTime().after(existing.getStartingTime())) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean doDaysOverlap(EventCategory a, EventCategory b) {
        if (a.getRepeatableType() == RepeatableType.DAILY ||
                b.getRepeatableType() == RepeatableType.DAILY) return true;

        if (!a.getRepeatable() && !b.getRepeatable()){
            return a.getBaseDate().equals(b.getBaseDate());
        }


        if (a.getRepeatsOnSpecificDay() != RepeatsOnSpecificDay.NO &&
                b.getRepeatsOnSpecificDay() != RepeatsOnSpecificDay.NO) {
            return a.getRepeatsOnSpecificDay() == b.getRepeatsOnSpecificDay();
        }

        return a.getBaseDate().getDayOfWeek() == b.getBaseDate().getDayOfWeek();
    }
}
