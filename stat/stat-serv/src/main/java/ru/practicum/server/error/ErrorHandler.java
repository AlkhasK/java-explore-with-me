package ru.practicum.server.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.practicum.server.error.model.ErrorResponse;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.DateTimeException;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class, DateTimeException.class, IllegalArgumentException.class,
            HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationError(Exception validationException) {
        log.warn(validationException.getMessage());
        return new ErrorResponse(validationException.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleServerError(Exception exception) {
        log.error("500", exception);
        var stringWriter = new StringWriter();
        exception.printStackTrace(new PrintWriter(stringWriter));
        return new ErrorResponse(stringWriter.toString());
    }

}
