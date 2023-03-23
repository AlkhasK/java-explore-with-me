package ru.practicum.main.error.exception;

public class ConditionNotMetException extends RuntimeException {

    public ConditionNotMetException(String message) {
        super(message);
    }
}
