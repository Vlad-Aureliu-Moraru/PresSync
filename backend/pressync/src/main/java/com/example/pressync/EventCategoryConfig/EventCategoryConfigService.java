package com.example.pressync.EventCategoryConfig;

import com.example.pressync.EventCategory.EventCategoryRepository;
import com.example.pressync.EventCategory.Model.RepeatableType;
import com.example.pressync.EventCategory.Model.RepeatsOnSpecificDay;
import com.example.pressync.EventCategoryConfig.Model.EventCategoryConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EventCategoryConfigService {
    private static final int DEFAULT_PREVIEW_LIMIT = 5;
    private final EventCategoryRepository eventCategoryRepository;

    public void validateConfig(EventCategoryConfig config) {
        if (config == null) {
            throw new IllegalArgumentException("Config is required");
        }
        if (config.getRepeatableType() == null) {
            throw new IllegalArgumentException("Repeatable type is required");
        }
        if (config.getRepeatsOnSpecificDay() == null) {
            throw new IllegalArgumentException("Repeats on specific day is required");
        }
        if (config.getBaseDate() == null) {
            throw new IllegalArgumentException("Base date is required");
        }

        enforceRepeatsOnSpecificDayAlignment(config);
    }

    public void enforceSingleCategoryPerConfig(Integer configId, Integer categoryIdToIgnore) {
        if (configId == null) {
            return;
        }
        long count = categoryIdToIgnore == null
                ? eventCategoryRepository.countByCategoryConfig_Id(configId)
                : eventCategoryRepository.countByCategoryConfig_IdAndIdNot(configId, categoryIdToIgnore);
        if (count > 0) {
            throw new IllegalArgumentException("Config already assigned to another category");
        }
    }

    public List<LocalDate> previewOccurrences(EventCategoryConfig config, int limit) {
        validateConfig(config);
        int safeLimit = limit > 0 ? limit : DEFAULT_PREVIEW_LIMIT;
        List<LocalDate> results = new ArrayList<>(safeLimit);

        LocalDate current = config.getBaseDate();
        while (results.size() < safeLimit) {
            if (matchesRepeatsOnSpecificDay(config, current)) {
                results.add(current);
            }
            current = nextCandidateDate(config, current);
        }

        return results;
    }

    private void enforceRepeatsOnSpecificDayAlignment(EventCategoryConfig config) {
        RepeatsOnSpecificDay repeatsOnSpecificDay = config.getRepeatsOnSpecificDay();
        if (repeatsOnSpecificDay == RepeatsOnSpecificDay.NO) {
            return;
        }
        DayOfWeek baseDay = config.getBaseDate().getDayOfWeek();
        if (!dayOfWeekForRepeatsOnSpecificDay(repeatsOnSpecificDay).equals(baseDay)) {
            throw new IllegalArgumentException("Base date does not match repeatsOnSpecificDay");
        }
    }

    private boolean matchesRepeatsOnSpecificDay(EventCategoryConfig config, LocalDate date) {
        RepeatsOnSpecificDay repeatsOnSpecificDay = config.getRepeatsOnSpecificDay();
        if (repeatsOnSpecificDay == RepeatsOnSpecificDay.NO) {
            return true;
        }
        return date.getDayOfWeek().equals(dayOfWeekForRepeatsOnSpecificDay(repeatsOnSpecificDay));
    }

    private LocalDate nextCandidateDate(EventCategoryConfig config, LocalDate current) {
        return switch (config.getRepeatableType()) {
            case DAILY -> current.plusDays(1);
            case WEEKLY -> current.plusWeeks(1);
            case BIWEEKLY -> current.plusWeeks(2);
            case MONTHLY -> current.plusMonths(1);
            case YEARLY -> current.plusYears(1);
        };
    }

    private DayOfWeek dayOfWeekForRepeatsOnSpecificDay(RepeatsOnSpecificDay repeatsOnSpecificDay) {
        return dayOfWeekMap().get(repeatsOnSpecificDay);
    }

    private Map<RepeatsOnSpecificDay, DayOfWeek> dayOfWeekMap() {
        Map<RepeatsOnSpecificDay, DayOfWeek> map = new EnumMap<>(RepeatsOnSpecificDay.class);
        map.put(RepeatsOnSpecificDay.MON, DayOfWeek.MONDAY);
        map.put(RepeatsOnSpecificDay.TUE, DayOfWeek.TUESDAY);
        map.put(RepeatsOnSpecificDay.WED, DayOfWeek.WEDNESDAY);
        map.put(RepeatsOnSpecificDay.THU, DayOfWeek.THURSDAY);
        map.put(RepeatsOnSpecificDay.FRI, DayOfWeek.FRIDAY);
        map.put(RepeatsOnSpecificDay.SAT, DayOfWeek.SATURDAY);
        map.put(RepeatsOnSpecificDay.SUN, DayOfWeek.SUNDAY);
        return map;
    }
}
