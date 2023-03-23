package ru.practicum.main.error;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.practicum.main.error.exception.*;
import ru.practicum.main.error.model.ApiError;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler({ObjectNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFoundError(Exception objectNotFoundException) {
        log.warn(objectNotFoundException.getMessage());
        ApiError apiError = new ApiError();
        apiError.setStatus(HttpStatus.NOT_FOUND);
        apiError.setMessage(objectNotFoundException.getMessage());
        apiError.setReason("The required object was not found");
        apiError.setTimestamp(LocalDateTime.now());
        apiError.setErrors(List.of(stackTraceToString(objectNotFoundException)));
        return apiError;
    }

    @ExceptionHandler({DataIntegrityViolationException.class, ConstraintViolationException.class,
            ParticipationRequestUserViolationException.class, ParticipationRequestEventStatusViolationException.class,
            ParticipationRequestParticipantLimitViolationException.class, CategoryConstraintViolationException.class,
            EventConditionNotMetException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleConstraintViolation(Exception constraintViolationException) {
        log.warn(constraintViolationException.getMessage());
        ApiError apiError = new ApiError();
        apiError.setStatus(HttpStatus.CONFLICT);
        apiError.setMessage(constraintViolationException.getMessage());
        apiError.setReason("Integrity constraint has been violated");
        apiError.setTimestamp(LocalDateTime.now());
        apiError.setErrors(List.of(stackTraceToString(constraintViolationException)));
        return apiError;
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, MethodArgumentTypeMismatchException.class,
            HttpMessageNotReadableException.class, IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleArgumentNotValid(Exception argumentNotValidException) {
        log.warn(argumentNotValidException.getMessage());
        ApiError apiError = new ApiError();
        apiError.setStatus(HttpStatus.BAD_REQUEST);
        apiError.setMessage(argumentNotValidException.getMessage());
        apiError.setReason("Incorrectly made request");
        apiError.setTimestamp(LocalDateTime.now());
        apiError.setErrors(List.of(stackTraceToString(argumentNotValidException)));
        return apiError;
    }

    @ExceptionHandler({ConditionNotMetException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiError handleConditionNotMet(Exception conditionNotMetException) {
        log.warn(conditionNotMetException.getMessage());
        ApiError apiError = new ApiError();
        apiError.setStatus(HttpStatus.FORBIDDEN);
        apiError.setMessage(conditionNotMetException.getMessage());
        apiError.setReason("For the requested operation the conditions are not met");
        apiError.setTimestamp(LocalDateTime.now());
        apiError.setErrors(List.of(stackTraceToString(conditionNotMetException)));
        return apiError;
    }

    private String stackTraceToString(Exception e) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        e.printStackTrace(printWriter);
        return stringWriter.toString();
    }
}
