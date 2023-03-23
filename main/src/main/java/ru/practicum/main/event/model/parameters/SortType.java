package ru.practicum.main.event.model.parameters;

import java.util.Optional;

public enum SortType {
    EVENT_DATE, VIEWS;

    public static Optional<SortType> of(String str) {
        for (SortType sortType : values()) {
            if (sortType.name().equals(str)) {
                return Optional.of(sortType);
            }
        }
        return Optional.empty();
    }
}
