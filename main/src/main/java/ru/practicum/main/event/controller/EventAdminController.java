package ru.practicum.main.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.event.helper.parameters.AdminSearchEventsParameters;
import ru.practicum.main.event.model.State;
import ru.practicum.main.event.model.dto.EventFullDto;
import ru.practicum.main.event.model.dto.UpdateEventAdminRequest;
import ru.practicum.main.event.service.EventAdminService;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/events")
public class EventAdminController {

    private final EventAdminService eventService;

    @GetMapping
    public List<EventFullDto> findAll(@RequestParam(required = false) List<Long> users,
                                      @RequestParam(required = false) List<String> states,
                                      @RequestParam(required = false) List<Long> categories,
                                      @RequestParam(required = false)
                                      @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                      @RequestParam(required = false)
                                      @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                      @RequestParam(defaultValue = "0") Integer from,
                                      @RequestParam(defaultValue = "10") Integer size) {
        log.info("GET : admin find all events with criteria users : {}, states : {}, " +
                        "categories : {}, rangeStart : {}, rangeEnd : {}, from : {}, size : {}",
                users, states, categories, rangeStart, rangeEnd, from, size);
        AdminSearchEventsParameters parameters;
        if (states == null) {
            parameters = new AdminSearchEventsParameters(users, null, categories, rangeStart, rangeEnd);
        } else {
            parameters = new AdminSearchEventsParameters(users, State.of(states), categories, rangeStart, rangeEnd);
        }
        return eventService.getEvents(parameters, from, size);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto update(@PathVariable Long eventId,
                               @RequestBody UpdateEventAdminRequest updateEventAdminRequest) {
        log.info("PATCH : admin update event id : {} update value : {}", eventId, updateEventAdminRequest);
        return eventService.updateEvent(eventId, updateEventAdminRequest);
    }
}
