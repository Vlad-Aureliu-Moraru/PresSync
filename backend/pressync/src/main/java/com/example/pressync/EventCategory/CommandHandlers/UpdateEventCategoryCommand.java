package com.example.pressync.EventCategory.CommandHandlers;

import com.example.pressync.Command;
import com.example.pressync.EventCategory.EventCategoryRepository;
import com.example.pressync.EventCategory.Model.EventCategory;
import com.example.pressync.EventCategory.Model.EventCategoryUpdateDTO;
import com.example.pressync.EventCategory.Model.DTO.UpdateEventCategoryRequest;
import com.example.pressync.EventCategoryConfig.EventCategoryConfigRepository;
import com.example.pressync.EventCategoryConfig.Model.EventCategoryConfig;
import com.example.pressync.EventCategory.Model.RepeatableType;
import com.example.pressync.EventCategory.Model.RepeatsOnSpecificDay;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.time.LocalDate;
import java.util.Calendar;
import java.net.http.HttpResponse;
@Service
public class UpdateEventCategoryCommand implements Command<EventCategoryUpdateDTO,String> {
    private final EventCategoryRepository eventCategoryRepository;
    private final EventCategoryConfigRepository eventCategoryConfigRepository;
    
    public UpdateEventCategoryCommand(EventCategoryRepository eventCategoryRepository, EventCategoryConfigRepository eventCategoryConfigRepository) {
        this.eventCategoryRepository = eventCategoryRepository;
        this.eventCategoryConfigRepository = eventCategoryConfigRepository;
    }

    @Override
    public ResponseEntity<String> execute(EventCategoryUpdateDTO dto) {
        int id = dto.getId();
        UpdateEventCategoryRequest request = dto.getRequest();
        
        EventCategory eventCategory = eventCategoryRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Event category with id " + id + " does not exist."));

        eventCategory.setName(request.name());
        eventCategory.setStartingTime(request.startingTime());
        eventCategory.setEndTime(request.endTime());
        eventCategory.setAttendanceTimeStart(request.attendanceTimeStart());
        eventCategory.setAttendanceDuration(request.attendanceDuration());
        eventCategory.setRepeatable(request.repeatable());

        if (Boolean.TRUE.equals(request.repeatable())) {
            EventCategoryConfig config;
            if (request.configId() != null) {
                config = eventCategoryConfigRepository.findById(request.configId())
                        .orElseThrow(() -> new IllegalArgumentException("Config not found"));
            } else {
                if (eventCategory.getCategoryConfig() != null) {
                   config = eventCategory.getCategoryConfig();
                } else {
                   config = new EventCategoryConfig();
                }
                config.setRepeatableType(request.repeatableType());
                config.setRepeatsOnSpecificDay(request.repeatsOnSpecificDay());
                config.setBaseDate(request.baseDate() != null ? request.baseDate() : LocalDate.now());
                config = eventCategoryConfigRepository.save(config);
            }
            eventCategory.setCategoryConfig(config);
        } else {
            eventCategory.setCategoryConfig(null);
            eventCategory.setDate(request.baseDate() != null ? request.baseDate() : LocalDate.now());
        }

        checkValidity(eventCategory);
        if (isColidingWithSomething(eventCategory)){
            throw new IllegalArgumentException("Coliding with something");
        }

        eventCategoryRepository.save(eventCategory);
        return ResponseEntity.ok().build();
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
        RepeatableType typeA = getRepeatableTypeSafe(a);
        RepeatableType typeB = getRepeatableTypeSafe(b);

        if (typeA == RepeatableType.DAILY || typeB == RepeatableType.DAILY) return true;

        if (!a.getRepeatable() && !b.getRepeatable()){
            return a.getDate().equals(b.getDate());
        }

        RepeatsOnSpecificDay dayA = getRepeatsOnSpecificDaySafe(a);
        RepeatsOnSpecificDay dayB = getRepeatsOnSpecificDaySafe(b);

        if (dayA != RepeatsOnSpecificDay.NO && dayB != RepeatsOnSpecificDay.NO && dayA != null && dayB != null) {
            return dayA == dayB;
        }

        return getBaseDateSafe(a).getDayOfWeek() == getBaseDateSafe(b).getDayOfWeek();
    }

    private RepeatableType getRepeatableTypeSafe(EventCategory cat) {
        return (cat.getRepeatable() && cat.getCategoryConfig() != null) ? cat.getCategoryConfig().getRepeatableType() : null;
    }
    
    private RepeatsOnSpecificDay getRepeatsOnSpecificDaySafe(EventCategory cat) {
        return (cat.getRepeatable() && cat.getCategoryConfig() != null) ? cat.getCategoryConfig().getRepeatsOnSpecificDay() : RepeatsOnSpecificDay.NO;
    }

    private LocalDate getBaseDateSafe(EventCategory cat) {
        return cat.getRepeatable() ? cat.getCategoryConfig().getBaseDate() : cat.getDate();
    }
}
