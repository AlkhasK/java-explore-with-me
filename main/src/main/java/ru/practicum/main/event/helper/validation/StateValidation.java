package ru.practicum.main.event.helper.validation;

import ru.practicum.main.error.exception.EventConditionNotMetException;
import ru.practicum.main.event.model.State;

import java.util.List;

public class StateValidation {

    public static void validateEventUpdateUser(State state) {
        if (!List.of(State.PENDING, State.CANCELED).contains(state)) {
            throw new EventConditionNotMetException("Only pending or canceled events can be changed");
        }
    }

    public static void validateEventUpdateAdmin(State currentState, State newState) {
        if (newState == State.PUBLISHED) {
            if (currentState != State.PENDING) {
                throw new EventConditionNotMetException(
                        String.format("Cannot publish event because it's not in right state: %s", currentState));
            }
        } else if (newState == State.CANCELED) {
            if (currentState == State.PUBLISHED) {
                throw new EventConditionNotMetException(
                        String.format("Cannot cancel event because it's not in right state: %s", currentState));
            }
        }
    }
}
