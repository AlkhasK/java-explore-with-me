package ru.practicum.main.event.helper.validation;

import ru.practicum.main.error.exception.EventConditionNotMetException;

import java.time.LocalDateTime;
import java.util.Objects;

public class EventDateValidation {

    private final static int MIN_DELTA_HOURS_EVENT_DATE_CURRENT_DATE = 2;

    private final static int MIN_DELTA_HOURS_EVENT_DATE_PUBLISHED = 1;

    public static void validateEventCreateUpdateUser(LocalDateTime eventDate) {
        if (Objects.nonNull(eventDate) && !eventDate.isAfter(LocalDateTime.now().plusHours(MIN_DELTA_HOURS_EVENT_DATE_CURRENT_DATE))) {
            throw new EventConditionNotMetException(String.format("Field: eventDate. Error: должно содержать дату, " +
                    "которая еще не наступила. Value: %s", eventDate));
        }
    }

    public static void validateEventUpdateAdmin(LocalDateTime eventDate, LocalDateTime publishOn) {
        if (eventDate.minusHours(MIN_DELTA_HOURS_EVENT_DATE_PUBLISHED).isBefore(publishOn)) {
            throw new EventConditionNotMetException(String.format("Field: eventDate. Error: должно содержать дату, " +
                    "которая еще не наступила. Value: %s", eventDate));
        }
    }

    public static void validateEventUpdateAdmin(LocalDateTime eventDate) {
        if (eventDate.isBefore(LocalDateTime.now())) {
            throw new EventConditionNotMetException(String.format("Field: eventDate. Error: должно содержать дату, " +
                    "которая еще не наступила. Value: %s", eventDate));
        }
    }
}
