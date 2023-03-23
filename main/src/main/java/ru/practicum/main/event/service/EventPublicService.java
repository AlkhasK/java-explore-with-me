package ru.practicum.main.event.service;

import ru.practicum.main.event.helper.parameters.PublicSearchEventsParameters;
import ru.practicum.main.event.model.dto.EventFullDto;
import ru.practicum.main.event.model.dto.EventShortDto;

import java.util.List;

public interface EventPublicService {
    List<EventShortDto> getEvents(PublicSearchEventsParameters parameters, Integer from, Integer size);

    EventFullDto getEvent(Long eventId);
}
