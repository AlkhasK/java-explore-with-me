package ru.practicum.main.event.model;

import ru.practicum.main.event.model.dto.state.StateActionAdmin;
import ru.practicum.main.event.model.dto.state.StateActionUser;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public enum State {
    PENDING, PUBLISHED, CANCELED;

    public static State of(@Nullable StateActionUser stateActionUser) {
        if (stateActionUser == null) {
            return null;
        }
        if (stateActionUser == StateActionUser.CANCEL_REVIEW) {
            return CANCELED;
        } else {
            return PENDING;
        }
    }

    public static State of(@Nullable StateActionAdmin stateActionAdmin) {
        if (stateActionAdmin == null) {
            return null;
        }
        if (stateActionAdmin == StateActionAdmin.REJECT_EVENT) {
            return CANCELED;
        } else {
            return PUBLISHED;
        }
    }

    public static Optional<State> of(String str) {
        for (State state : values()) {
            if (state.name().equals(str)) {
                return Optional.of(state);
            }
        }
        return Optional.empty();
    }

    public static List<State> of(List<String> strings) {
        return strings.stream()
                .map(str -> of(str).orElseThrow(
                        () -> new IllegalArgumentException(
                                String.format("Failed to convert value '%s' of type String  to State", str))))
                .collect(Collectors.toList());
    }
}
