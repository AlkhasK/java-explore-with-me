package ru.practicum.main.participation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.error.exception.ObjectNotFoundException;
import ru.practicum.main.error.exception.ParticipationRequestEventStatusViolationException;
import ru.practicum.main.error.exception.ParticipationRequestParticipantLimitViolationException;
import ru.practicum.main.error.exception.ParticipationRequestUserViolationException;
import ru.practicum.main.event.model.Event;
import ru.practicum.main.event.model.State;
import ru.practicum.main.event.storage.EventStorage;
import ru.practicum.main.participation.model.ParticipationRequest;
import ru.practicum.main.participation.model.Status;
import ru.practicum.main.participation.model.dto.ParticipationRequestDto;
import ru.practicum.main.participation.model.dto.ParticipationRequestMapper;
import ru.practicum.main.participation.storage.ParticipationRequestStorage;
import ru.practicum.main.user.model.User;
import ru.practicum.main.user.storage.UserStorage;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ParticipationRequestServiceImpl implements ParticipationRequestPrivateService {

    private final static int UNLIMITED_PARTICIPATION_LIMIT = 0;

    private final ParticipationRequestStorage participationRequestStorage;
    private final ParticipationRequestMapper participationRequestMapper;
    private final UserStorage userStorage;
    private final EventStorage eventStorage;

    @Override
    public List<ParticipationRequestDto> getParticipationRequests(Long userId) {
        userStorage.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("User with id=%s was not found", userId)));
        List<ParticipationRequest> participationRequests = participationRequestStorage.findAllWithRequester(userId);
        return participationRequests.stream()
                .map(participationRequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public ParticipationRequestDto createParticipationRequest(Long userId, Long eventId) {
        User requester = userStorage.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("User with id=%s was not found", userId)));
        Event event = eventStorage.findWithCategoryAndInitiator(eventId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Event with id=%s was not found", eventId)));
        if (userId.equals(event.getInitiator().getId())) {
            throw new ParticipationRequestUserViolationException(String.format("Event owner id : %s can't create " +
                    "participation request for that event id : %s", userId, eventId));
        }
        if (event.getState() != State.PUBLISHED) {
            throw new ParticipationRequestEventStatusViolationException(String.format("Participation request can't be " +
                    "created for event id : %s state : %s", eventId, event.getState()));
        }
        validateParticipationLimit(event);
        ParticipationRequest participationRequest = new ParticipationRequest();
        participationRequest.setRequester(requester);
        participationRequest.setEvent(event);
        participationRequest.setCreated(LocalDateTime.now());
        participationRequest.setStatus(calculateParticipationStatus(event));
        ParticipationRequest createdParticipationRequest = participationRequestStorage.save(participationRequest);
        return participationRequestMapper.toParticipationRequestDto(createdParticipationRequest);
    }

    private void validateParticipationLimit(Event event) {
        if (event.getParticipantLimit().equals(UNLIMITED_PARTICIPATION_LIMIT)) {
            return;
        }
        List<Status> statusAcceptedRequests = event.getRequestModeration() ? List.of(Status.CONFIRMED)
                : List.of(Status.CONFIRMED, Status.PENDING);
        int participationRequestConfirmedCount = participationRequestStorage
                .countAllByStatusInAndEvent_Id(statusAcceptedRequests, event.getId()).intValue();
        if (event.getParticipantLimit().equals(participationRequestConfirmedCount)) {
            throw new ParticipationRequestParticipantLimitViolationException("The participant limit has been reached");
        }
    }

    private Status calculateParticipationStatus(Event event) {
        if (!event.getRequestModeration()) {
            return Status.CONFIRMED;
        }
        if (event.getParticipantLimit().equals(UNLIMITED_PARTICIPATION_LIMIT)) {
            return Status.CONFIRMED;
        }
        return Status.PENDING;
    }

    @Transactional
    @Override
    public ParticipationRequestDto cancelParticipationRequest(Long userId, Long requestId) {
        userStorage.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("User with id=%s was not found", userId)));
        ParticipationRequest participationRequest = participationRequestStorage.findByIdAndRequester_Id(requestId, userId)
                .orElseThrow(() -> new ObjectNotFoundException(
                        String.format("Participation request with id=%s was not found", requestId)));
        participationRequest.setStatus(Status.CANCELED);
        ParticipationRequest updatedParticipationRequest = participationRequestStorage.save(participationRequest);
        return participationRequestMapper.toParticipationRequestDto(updatedParticipationRequest);
    }
}
