package com.example.pressync.EventCategory.CommandHandlers;

import com.example.pressync.Command;
import com.example.pressync.EventCategory.EventCategoryRepository;
import com.example.pressync.EventCategory.Model.EventCategory;
import com.example.pressync.EventCategory.Model.EventCategoryChangedEvent;
import com.example.pressync.EventCategory.Model.RepeatableType;
import com.example.pressync.EventCategory.Model.RepeatsOnSpecificDay;
import com.example.pressync.EventCategory.Model.DTO.CreateEventCategoryRequest;
import com.example.pressync.EventCategoryConfig.EventCategoryConfigRepository;
import com.example.pressync.EventCategoryConfig.Model.EventCategoryConfig;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.time.LocalDate;
import java.util.Calendar;

@Service
@RequiredArgsConstructor
public class CreateEventCategoryCommand implements Command<CreateEventCategoryRequest,String> {
    private final EventCategoryRepository eventCategoryRepository;
    private final EventCategoryConfigRepository eventCategoryConfigRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    @Override
    @Transactional
    public ResponseEntity<String> execute(CreateEventCategoryRequest request) {

        EventCategory entity = new EventCategory();
        entity.setName(request.name());
        entity.setStartingTime(request.startingTime());
        entity.setEndTime(request.endTime());
        entity.setAttendanceTimeStart(request.attendanceTimeStart());
        entity.setAttendanceDuration(request.attendanceDuration());
        entity.setRepeatable(request.repeatable());

        if (Boolean.TRUE.equals(request.repeatable())) {
            EventCategoryConfig config;
            if (request.configId() != null) {
                config = eventCategoryConfigRepository.findById(request.configId())
                        .orElseThrow(() -> new IllegalArgumentException("Config not found"));
            } else {
                config = new EventCategoryConfig();
                config.setRepeatableType(request.repeatableType());
                config.setRepeatsOnSpecificDay(request.repeatsOnSpecificDay());
                config.setBaseDate(request.baseDate() != null ? request.baseDate() : LocalDate.now());
                config = eventCategoryConfigRepository.save(config);
            }
            entity.setCategoryConfig(config);
        } else {
            entity.setSpecificDate(request.baseDate() != null ? request.baseDate() : LocalDate.now());
        }

        checkValidity(entity);
        if (isColidingWithSomething(entity)){
            throw new IllegalArgumentException("Coliding with something");
        };


        eventCategoryRepository.save(entity);
        applicationEventPublisher.publishEvent(new EventCategoryChangedEvent(entity));
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
        RepeatableType typeA = getRepeatableTypeSafe(a);
        RepeatableType typeB = getRepeatableTypeSafe(b);

        if (typeA == RepeatableType.DAILY || typeB == RepeatableType.DAILY) return true;

        if (!a.getRepeatable() && !b.getRepeatable()){
            return a.getSpecificDate().equals(b.getSpecificDate());
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
        return cat.getRepeatable() ? cat.getCategoryConfig().getBaseDate() : cat.getSpecificDate();
    }
}
