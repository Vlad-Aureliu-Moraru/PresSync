package com.example.pressync.EventCategoryConfig;

import com.example.pressync.EventCategory.EventCategoryRepository;
import com.example.pressync.EventCategory.Model.RepeatableType;
import com.example.pressync.EventCategory.Model.RepeatsOnSpecificDay;
import com.example.pressync.EventCategoryConfig.Model.EventCategoryConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventCategoryConfigServiceTest {

    @Mock
    private EventCategoryRepository eventCategoryRepository;

    private EventCategoryConfigService eventCategoryConfigService;

    @BeforeEach
    void setUp() {
        eventCategoryConfigService = new EventCategoryConfigService(eventCategoryRepository);
    }

    @Test
    void validateConfigRequiresFields() {
        EventCategoryConfig config = new EventCategoryConfig();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> eventCategoryConfigService.validateConfig(config));

        assertEquals("Repeatable type is required", exception.getMessage());
    }

    @Test
    void validateConfigRejectsMismatchedRepeatDay() {
        EventCategoryConfig config = new EventCategoryConfig();
        config.setRepeatableType(RepeatableType.WEEKLY);
        config.setRepeatsOnSpecificDay(RepeatsOnSpecificDay.MON);
        config.setBaseDate(LocalDate.of(2026, 4, 29));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> eventCategoryConfigService.validateConfig(config));

        assertEquals("Base date does not match repeatsOnSpecificDay", exception.getMessage());
    }

    @Test
    void enforceSingleCategoryPerConfigRejectsReuse() {
        when(eventCategoryRepository.countByCategoryConfig_Id(10)).thenReturn(1L);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> eventCategoryConfigService.enforceSingleCategoryPerConfig(10, null));

        assertEquals("Config already assigned to another category", exception.getMessage());
    }

    @Test
    void enforceSingleCategoryPerConfigAllowsReuseWhenIgnoringSameCategory() {
        when(eventCategoryRepository.countByCategoryConfig_IdAndIdNot(10, 3)).thenReturn(0L);

        assertDoesNotThrow(() -> eventCategoryConfigService.enforceSingleCategoryPerConfig(10, 3));
    }

    @Test
    void previewOccurrencesDailyWithSpecificDay() {
        EventCategoryConfig config = new EventCategoryConfig();
        config.setRepeatableType(RepeatableType.DAILY);
        config.setRepeatsOnSpecificDay(RepeatsOnSpecificDay.MON);
        config.setBaseDate(LocalDate.of(2026, 4, 27));

        List<LocalDate> preview = eventCategoryConfigService.previewOccurrences(config, 3);

        assertEquals(List.of(
                LocalDate.of(2026, 4, 27),
                LocalDate.of(2026, 5, 4),
                LocalDate.of(2026, 5, 11)
        ), preview);
    }

    @Test
    void previewOccurrencesMonthly() {
        EventCategoryConfig config = new EventCategoryConfig();
        config.setRepeatableType(RepeatableType.MONTHLY);
        config.setRepeatsOnSpecificDay(RepeatsOnSpecificDay.NO);
        config.setBaseDate(LocalDate.of(2026, 1, 31));

        List<LocalDate> preview = eventCategoryConfigService.previewOccurrences(config, 2);

        assertEquals(LocalDate.of(2026, 1, 31), preview.get(0));
        assertEquals(LocalDate.of(2026, 2, 28), preview.get(1));
    }
}
