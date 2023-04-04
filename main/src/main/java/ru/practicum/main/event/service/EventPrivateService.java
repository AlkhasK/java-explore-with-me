package ru.practicum.main.event.service;

import ru.practicum.main.comment.model.dto.CommentDto;
import ru.practicum.main.comment.model.dto.NewCommentDto;
import ru.practicum.main.event.model.dto.*;
import ru.practicum.main.participation.model.dto.ParticipationRequestDto;

import java.util.List;

public interface EventPrivateService {
    EventFullDto createEvent(Long userId, NewEventDto newEventDto);

    List<EventShortDto> getEvents(Long userId, Integer from, Integer size);

    EventFullDto getEvent(Long userId, Long eventId);

    EventFullDto updateEvent(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest);

    List<ParticipationRequestDto> getParticipationRequests(Long userId, Long eventId);

    EventRequestStatusUpdateResult updateRequestStatus(Long userId, Long eventId, EventRequestStatusUpdateRequest updateRequest);

    CommentDto moderateComment(Long userId, Long eventId, Long commentId, NewCommentDto newCommentDto);

    void deleteComment(Long userId, Long eventId, Long commentId);
}
