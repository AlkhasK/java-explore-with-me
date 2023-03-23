package ru.practicum.main.event.service;

import ru.practicum.main.event.helper.parameters.AdminSearchEventsParameters;
import ru.practicum.main.event.model.dto.EventFullDto;
import ru.practicum.main.event.model.dto.UpdateEventAdminRequest;

import java.util.List;

public interface EventAdminService {
    List<EventFullDto> getEvents(AdminSearchEventsParameters parameters, Integer from, Integer size);

    EventFullDto updateEvent(Long eventId, UpdateEventAdminRequest updateEventAdminRequest);
}
