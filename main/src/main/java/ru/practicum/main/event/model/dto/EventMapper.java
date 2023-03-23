package ru.practicum.main.event.model.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.main.category.model.Category;
import ru.practicum.main.category.model.dto.CategoryDto;
import ru.practicum.main.category.model.dto.CategoryMapper;
import ru.practicum.main.event.location.model.dto.LocationDto;
import ru.practicum.main.event.location.model.dto.LocationMapper;
import ru.practicum.main.event.model.Event;
import ru.practicum.main.event.model.State;
import ru.practicum.main.user.model.User;
import ru.practicum.main.user.model.dto.UserMapper;
import ru.practicum.main.user.model.dto.UserShortDto;

import javax.annotation.Nullable;

@Component
@RequiredArgsConstructor
public class EventMapper {

    private final LocationMapper locationMapper;
    private final CategoryMapper categoryMapper;
    private final UserMapper userMapper;

    public Event toEvent(NewEventDto newEventDto, Category category, User initiator) {
        Event event = new Event();
        event.setAnnotation(newEventDto.getAnnotation());
        event.setCategory(category);
        event.setDescription(newEventDto.getDescription());
        event.setEventDate(newEventDto.getEventDate());
        event.setLocation(locationMapper.toLocation(newEventDto.getLocation()));
        event.setPaid(newEventDto.getPaid());
        event.setParticipantLimit(newEventDto.getParticipantLimit());
        event.setRequestModeration(newEventDto.getRequestModeration());
        event.setTitle(newEventDto.getTitle());
        event.setInitiator(initiator);
        return event;
    }

    public Event toEvent(UpdateEventUserRequest updateEventUserRequest, @Nullable Category category) {
        Event event = new Event();
        event.setAnnotation(updateEventUserRequest.getAnnotation());
        event.setCategory(category);
        event.setDescription(updateEventUserRequest.getDescription());
        event.setEventDate(updateEventUserRequest.getEventDate());
        LocationDto locationDto = updateEventUserRequest.getLocation();
        event.setLocation(locationDto == null ? null : locationMapper.toLocation(locationDto));
        event.setPaid(updateEventUserRequest.getPaid());
        event.setParticipantLimit(updateEventUserRequest.getParticipantLimit());
        event.setState(State.of(updateEventUserRequest.getStateAction()));
        event.setTitle(updateEventUserRequest.getTitle());
        return event;
    }

    public EventFullDto toEventFullDto(Event event) {
        EventFullDto eventFullDto = new EventFullDto();
        eventFullDto.setAnnotation(event.getAnnotation());
        CategoryDto categoryDto = categoryMapper.toCategoryDto(event.getCategory());
        eventFullDto.setCategory(categoryDto);
        eventFullDto.setCreatedOn(event.getCreatedOn());
        eventFullDto.setDescription(event.getDescription());
        eventFullDto.setEventDate(event.getEventDate());
        eventFullDto.setId(event.getId());
        UserShortDto initiatorDto = userMapper.toUserShortDto(event.getInitiator());
        eventFullDto.setInitiator(initiatorDto);
        LocationDto locationDto = locationMapper.toLocationDto(event.getLocation());
        eventFullDto.setLocation(locationDto);
        eventFullDto.setPaid(event.getPaid());
        eventFullDto.setParticipantLimit(event.getParticipantLimit());
        eventFullDto.setPublishedOn(event.getPublishedOn());
        eventFullDto.setRequestModeration(event.getRequestModeration());
        eventFullDto.setState(event.getState());
        eventFullDto.setTitle(event.getTitle());
        return eventFullDto;
    }

    public EventShortDto toEventShortDto(Event event) {
        EventShortDto eventShortDto = new EventShortDto();
        eventShortDto.setAnnotation(event.getAnnotation());
        CategoryDto categoryDto = categoryMapper.toCategoryDto(event.getCategory());
        eventShortDto.setCategory(categoryDto);
        eventShortDto.setEventDate(event.getEventDate());
        eventShortDto.setId(event.getId());
        UserShortDto initiatorDto = userMapper.toUserShortDto(event.getInitiator());
        eventShortDto.setInitiator(initiatorDto);
        eventShortDto.setPaid(event.getPaid());
        eventShortDto.setTitle(event.getTitle());
        return eventShortDto;
    }

    public Event toEvent(UpdateEventAdminRequest updateEventAdminRequest, @Nullable Category category) {
        Event event = new Event();
        event.setAnnotation(updateEventAdminRequest.getAnnotation());
        event.setCategory(category);
        event.setDescription(updateEventAdminRequest.getDescription());
        event.setEventDate(updateEventAdminRequest.getEventDate());
        LocationDto locationDto = updateEventAdminRequest.getLocation();
        event.setLocation(locationDto == null ? null : locationMapper.toLocation(locationDto));
        event.setPaid(updateEventAdminRequest.getPaid());
        event.setParticipantLimit(updateEventAdminRequest.getParticipantLimit());
        event.setRequestModeration(updateEventAdminRequest.getRequestModeration());
        event.setState(State.of(updateEventAdminRequest.getStateAction()));
        event.setTitle(updateEventAdminRequest.getTitle());
        return event;
    }
}
