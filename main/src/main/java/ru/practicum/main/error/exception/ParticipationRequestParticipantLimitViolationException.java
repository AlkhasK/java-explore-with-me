package ru.practicum.main.error.exception;

public class ParticipationRequestParticipantLimitViolationException extends RuntimeException {

    public ParticipationRequestParticipantLimitViolationException(String message) {
        super(message);
    }
}
