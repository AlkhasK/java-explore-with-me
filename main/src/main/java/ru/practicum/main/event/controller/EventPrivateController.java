package ru.practicum.main.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.comment.model.dto.CommentDto;
import ru.practicum.main.comment.model.dto.NewCommentDto;
import ru.practicum.main.event.helper.validation.EventDateValidation;
import ru.practicum.main.event.model.dto.*;
import ru.practicum.main.event.service.EventPrivateService;
import ru.practicum.main.participation.model.dto.ParticipationRequestDto;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/events")
public class EventPrivateController {

    private final EventPrivateService eventService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEvent(@PathVariable Long userId,
                                    @Valid @RequestBody NewEventDto newEventDto) {
        EventDateValidation.validateEventCreateUpdateUser(newEventDto.getEventDate());
        log.info("POST : create event : {}", newEventDto);
        return eventService.createEvent(userId, newEventDto);
    }

    @GetMapping
    public List<EventShortDto> findAll(@PathVariable Long userId,
                                       @RequestParam(defaultValue = "0") Integer from,
                                       @RequestParam(defaultValue = "10") Integer size) {
        log.info("GET : user id : {} find all events", userId);
        return eventService.getEvents(userId, from, size);
    }

    @GetMapping("/{eventId}")
    public EventFullDto find(@PathVariable Long userId,
                             @PathVariable Long eventId) {
        log.info("GET : user id : {} find event id : {}", userId, eventId);
        return eventService.getEvent(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto update(@PathVariable Long userId,
                               @PathVariable Long eventId,
                               @Valid @RequestBody UpdateEventUserRequest updateEventUserRequest) {
        EventDateValidation.validateEventCreateUpdateUser(updateEventUserRequest.getEventDate());
        log.info("PATCH : user id : {} update event id : {} update value : {}", userId, eventId, updateEventUserRequest);
        return eventService.updateEvent(userId, eventId, updateEventUserRequest);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> findAll(@PathVariable Long userId, @PathVariable Long eventId) {
        log.info("GET : user id : {} find participation requests for event id : {}", userId, eventId);
        return eventService.getParticipationRequests(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult updateRequestStatus(@PathVariable Long userId,
                                                              @PathVariable Long eventId,
                                                              @Valid @RequestBody EventRequestStatusUpdateRequest updateRequest) {
        log.info("PATCH : user id : {} event id : {} update request statuse : {}", userId, eventId, updateRequest);
        return eventService.updateRequestStatus(userId, eventId, updateRequest);
    }

    @PatchMapping("/{eventId}/comments/{commentId}")
    public CommentDto moderateComment(@PathVariable Long userId,
                                      @PathVariable Long eventId,
                                      @PathVariable Long commentId,
                                      @Valid @RequestBody NewCommentDto newCommentDto) {
        log.info("PATCH : user id : {} event id : {} moderate comment id : {} value : {}", userId,
                eventId, commentId, newCommentDto);
        return eventService.moderateComment(userId, eventId, commentId, newCommentDto);
    }

    @DeleteMapping("/{eventId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Long userId,
                              @PathVariable Long eventId,
                              @PathVariable Long commentId) {
        log.info("DELETE : user id : {} event id : {} delete comment id : {}", userId, eventId, commentId);
        eventService.deleteComment(userId, eventId, commentId);
    }
}
