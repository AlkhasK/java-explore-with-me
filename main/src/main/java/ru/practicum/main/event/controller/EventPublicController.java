package ru.practicum.main.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.event.helper.parameters.PublicSearchEventsParameters;
import ru.practicum.main.event.model.dto.EventFullDto;
import ru.practicum.main.event.model.dto.EventShortDto;
import ru.practicum.main.event.model.parameters.SortType;
import ru.practicum.main.event.service.EventPublicService;
import ru.practicum.main.stat.StatClient;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class EventPublicController {

    private final EventPublicService eventService;

    private final StatClient statClient;

    @GetMapping
    public List<EventShortDto> findAll(@RequestParam(required = false) String text,
                                       @RequestParam(required = false) List<Long> categories,
                                       @RequestParam(required = false) Boolean paid,
                                       @RequestParam(required = false)
                                       @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                       @RequestParam(required = false)
                                       @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                       @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                       @RequestParam(required = false) String sort,
                                       @RequestParam(defaultValue = "0") Integer from,
                                       @RequestParam(defaultValue = "10") Integer size,
                                       HttpServletRequest request) {
        PublicSearchEventsParameters parameters;
        if (sort == null) {
            parameters = new PublicSearchEventsParameters(text, categories, paid, rangeStart, rangeEnd, onlyAvailable,
                    null);
        } else {
            SortType sortType = SortType.of(sort).orElseThrow(() -> new IllegalArgumentException(
                    String.format("Failed to convert value '%s' of type String  to SortType", sort)));
            parameters = new PublicSearchEventsParameters(text, categories, paid, rangeStart, rangeEnd, onlyAvailable,
                    sortType);
        }
        log.info("GET : public find all events with criteria text : {}, categories : {}, " +
                        "paid : {}, rangeStart : {}, rangeEnd : {}, sort : {}, from : {}, size : {}",
                text, categories, paid, rangeStart, rangeEnd, sort, from, size);
        statClient.registerHit(request.getRemoteAddr(), request.getRequestURI());
        return eventService.getEvents(parameters, from, size);
    }

    @GetMapping("/{eventId}")
    public EventFullDto find(@PathVariable Long eventId, HttpServletRequest request) {
        log.info("GET : find event id : {}", eventId);
        EventFullDto eventFullDto = eventService.getEvent(eventId);
        statClient.registerHit(request.getRemoteAddr(), request.getRequestURI());
        return eventFullDto;
    }
}
