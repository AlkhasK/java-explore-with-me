package ru.practicum.main.error.exception;

public class ObjectNotFoundException extends RuntimeException {

    public ObjectNotFoundException(String message) {
        super(message);
    }
}
