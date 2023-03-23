package ru.practicum.main.error.exception;

public class CategoryConstraintViolationException extends RuntimeException {

    public CategoryConstraintViolationException(String message) {
        super(message);
    }
}
