package ru.practicum.main.error.exception;

public class EventConditionNotMetException extends RuntimeException {

    public EventConditionNotMetException(String message) {
        super(message);
    }
}
